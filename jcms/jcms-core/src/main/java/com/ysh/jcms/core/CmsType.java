package com.ysh.jcms.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.ysh.jcms.core.NativeBridge.Codec;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jcms base class.
 *
 * Two modes:
 *
 * 1. Leaf type (Boolean, Int8, etc.):
 *    - No children
 *    - Manages its own native memory, implements write()/read()/encode()/decode()
 *
 * 2. Container type (SEQUENCE, CHOICE, etc.):
 *    - children() returns the list of child fields
 *    - Base class auto-write/read: writes child.nativePtr sequentially into parent native memory
 *    - encode()/decode() handled by C side (requires C function support)
 */
public abstract class CmsType {

    private static final Logger log = LoggerFactory.getLogger(CmsType.class);

    public Pointer nativePtr;
    public int nativeSize;
    protected Codec codec;

    protected CmsType() {
        this.nativeSize = calcNativeSize();
        this.nativePtr = new Memory(nativeSize);
        zero();
    }

    protected CmsType(Codec codec) {
        this();
        this.codec = codec;
    }

    // ==================== Overridable by subclasses ====================

    /** Return child fields (overridden by container types; leaf types return empty list). */
    public List<? extends CmsType> children() {
        return Collections.emptyList();
    }

    /** Compute native memory size. Leaf types override; container types default to children.size() * 8. */
    protected int calcNativeSize() {
        return children().size() * 8;
    }

    /**
     * Sync Java fields -> native memory.
     * - Leaf types: write fields directly to nativePtr
     * - Container types: iterate children -> child.write() -> write child.nativePtr
     */
    public void write() {
        List<? extends CmsType> kids = children();
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            if (child == null) {
                nativePtr.setPointer(i * 8L, Pointer.NULL);
                continue;
            }
            child.write();
            nativePtr.setPointer(i * 8L, child.nativePtr);
        }
    }

    /**
     * Sync native memory -> Java fields.
     * - Leaf types: read fields from nativePtr
     * - Container types: read child pointer -> set child.nativePtr -> child.read()
     */
    public void read() {
        List<? extends CmsType> kids = children();
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            if (child == null) continue;
            Pointer ptr = nativePtr.getPointer(i * 8L);
            if (ptr != null) {
                child.nativePtr = ptr;
                child.read();
            }
        }
    }

    /**
     * PER encode: encodes the current structure to a byte array.
     * Uses {@link #codec} if set, otherwise throws.
     */
    public byte[] encode() {
        if (codec == null)
            throw new UnsupportedOperationException(
                getClass().getSimpleName() + " has no FFI encode (codec not set)");
        //log.debug("encode {} start", getClass().getSimpleName());
        write();
        byte[] result = codec.encode(nativePtr);
        //log.debug("encode {} OK, resultLen={}", getClass().getSimpleName(), result.length);
        return result;
    }

    /**
     * PER decode: decodes from a byte array into the current structure.
     * Uses {@link #codec} if set, otherwise throws.
     *
     * <p>Auto-retry decode: each decode attempt pre-allocates
     * {@link CmsArray#allocSize} slots per CmsArray in the tree. If a
     * SEQUENCE OF has more elements than slots, the C decoder writes the
     * actual count into {@code cms_array_t.count} then returns
     * {@code CMS_RETRY (-2)}.  Java reads the count, adjusts allocSize,
     * clears items, and retries.  Retries in a while loop until C returns
     * 0 (success) or a genuine error.
     */
    public void decode(byte[] data) {
        if (codec == null)
            throw new UnsupportedOperationException(
                getClass().getSimpleName() + " has no FFI decode (codec not set)");

        // log.debug("decode {} start, dataLen={}", getClass().getSimpleName(), data.length);

        int retry = 200;
        while (retry-- > 0) {
            // log.debug("decode {} retry={}", getClass().getSimpleName(), 200 - retry - 1);

            // Fresh native memory
            allocate();
            log.trace("  allocate: nativePtr={}, nativeSize={}", nativePtr, nativeSize);

            write();
            log.trace("  write done");

            int rc = codec.decodeRaw(nativePtr, data);
            // log.debug("  decodeRaw rc={}", rc);

            if (rc == 0) {
                log.trace("  calling read()...");
                read();
                // log.debug("decode {} OK", getClass().getSimpleName());
                return;
            }
            if (rc != -2)  // not CMS_RETRY → real error
                throw new RuntimeException(
                    "decode failed: rc=" + rc + " for " + getClass().getSimpleName());

            // CMS_RETRY: C wrote the actual count(s); read them and resize
            // log.debug("  CMS_RETRY: reading array counts from native memory");
            resize();
        }
        throw new RuntimeException(
            "decode failed: too many retries (200) for " + getClass().getSimpleName());
    }

    /**
     * After CMS_RETRY, resize the tree so that every CmsArray has enough
     * slots for the actual element count reported by C.
     * Default implementation: recurse into children.
     * CmsArray overrides this to read its native count and resize items.
     */
    /**
     * Which children to resize after CMS_RETRY.
     * Default: all children(). CHOICE types override to return only the
     * selected alternative, preventing expansion of unselected branches.
     */
    protected List<? extends CmsType> resizeList() {
        return children();
    }

    protected void resize() {
        List<? extends CmsType> kids = resizeList();
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            if (child != null) child.resize();
        }
    }

    /** Allocate (or re-allocate) native memory to a clean zeroed state. */
    private void allocate() {
        this.nativeSize = calcNativeSize();
        this.nativePtr = new Memory(nativeSize);
        zero();
    }

    // ==================== equals / hashCode / toString ====================

    @Override
    public boolean equals(Object o) {
        return CmsEqualityUtil.equals(this, o);
    }

    @Override
    public int hashCode() {
        return CmsEqualityUtil.hashCode(this);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    protected String toString(int depth) {
        // Build field name map for debug output
        java.util.Map<CmsType, String> fieldNames = new IdentityHashMap<>();
        for (java.lang.reflect.Field f : getClass().getFields()) {
            if (CmsType.class.isAssignableFrom(f.getType())) {
                try { fieldNames.put((CmsType) f.get(this), f.getName()); } catch (Exception e) {}
            }
        }
        return CmsFormatUtil.toString(this, depth, fieldNames);
    }

    // ==================== Memory helper ====================

    public void zero() {
        if (nativePtr != null) {
            nativePtr.clear((long) nativeSize);
        }
    }
}
