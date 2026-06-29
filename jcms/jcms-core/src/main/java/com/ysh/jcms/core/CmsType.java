package com.ysh.jcms.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.ysh.jcms.core.NativeBridge.Codec;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

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
        write();
        byte[] result = codec.encode(nativePtr);
        return result;
    }

    /**
     * PER decode: decodes from a byte array into the current structure.
     * Uses {@link #codec} if set, otherwise throws.
     */
    public void decode(byte[] data) {
        if (codec == null)
            throw new UnsupportedOperationException(
                getClass().getSimpleName() + " has no FFI decode (codec not set)");
        write();
        try {
            codec.decode(nativePtr, data);
        } catch (Exception e) {
            throw new RuntimeException("decode failed for " + getClass().getSimpleName(), e);
        }
        read();
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

    private String toString(int depth) {
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
