package com.ysh.jcms.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * jcms2 base class.
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

    protected CmsType() {
        this.nativeSize = calcNativeSize();
        this.nativePtr = new Memory(nativeSize);
        zero();
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
            Pointer ptr = nativePtr.getPointer(i * 8L);
            if (ptr != null) {
                child.nativePtr = ptr;
                child.read();
            }
        }
    }

    /**
     * PER encode: encodes the current structure to a byte array.
     * - Leaf types: write() then call C FFI
     * - Container types: write() first (recursively writes children), then call C FFI
     */
    public byte[] encode() {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no FFI encode");
    }

    /**
     * PER decode: decodes from a byte array into the current structure.
     * - Leaf types: call C FFI then read()
     * - Container types: call C FFI then read() (recursively reads children)
     */
    public void decode(byte[] data) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no FFI decode");
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CmsType other = (CmsType) o;

        List<? extends CmsType> kids = children();
        List<? extends CmsType> otherKids = other.children();

        if (!kids.isEmpty()) {
            // Compound type: compare children recursively
            if (kids.size() != otherKids.size()) return false;
            for (int i = 0; i < kids.size(); i++) {
                if (!kids.get(i).equals(otherKids.get(i))) return false;
            }
            return true;
        }

        // Leaf type: compare native memory bytes
        return Arrays.equals(
            nativePtr.getByteArray(0, nativeSize),
            other.nativePtr.getByteArray(0, other.nativeSize)
        );
    }

    @Override
    public int hashCode() {
        List<? extends CmsType> kids = children();
        if (!kids.isEmpty()) {
            int h = 1;
            for (CmsType child : kids) h = 31 * h + child.hashCode();
            return h;
        }
        return Arrays.hashCode(nativePtr.getByteArray(0, nativeSize));
    }

    // ==================== Utilities ====================

    public void zero() {
        if (nativePtr != null) {
            nativePtr.clear((long) nativeSize);
        }
    }
}
