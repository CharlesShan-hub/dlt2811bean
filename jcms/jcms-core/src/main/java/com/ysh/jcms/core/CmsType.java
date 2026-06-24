package com.ysh.jcms.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;
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
     * Uses {@link #codec} if set, otherwise throws.
     */
    public byte[] encode() {
        if (codec == null)
            throw new UnsupportedOperationException(
                getClass().getSimpleName() + " has no FFI encode (codec not set)");
        write();
        return codec.encode(nativePtr);
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
        codec.decode(nativePtr, data);
        read();
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

    // ==================== toString ====================

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        List<? extends CmsType> kids = children();
        if (!kids.isEmpty()) {
            String indent = repeat("    ", depth + 1);
            String bracketIndent = repeat("    ", depth);
            StringBuilder sb = new StringBuilder("(").append(getClass().getSimpleName()).append(") {\n");
            // Map children to their Java field names (public fields only, toString aid)
            java.util.Map<CmsType, String> fieldNames = new java.util.HashMap<>();
            for (java.lang.reflect.Field f : getClass().getFields()) {
                if (CmsType.class.isAssignableFrom(f.getType())) {
                    try { fieldNames.put((CmsType) f.get(this), f.getName()); } catch (Exception e) {}
                }
            }
            for (int i = 0; i < kids.size(); i++) {
                CmsType child = kids.get(i);
                String name = fieldNames.getOrDefault(child, "[" + i + "]");
                String val = child.toString(depth + 1);
                sb.append(indent).append("[").append(i).append("] ").append(name).append(": ").append(val).append(",\n");
            }
            if (!kids.isEmpty()) {
                sb.setLength(sb.length() - 2);
                sb.append("\n");
            }
            sb.append(bracketIndent).append("}");
            return sb.toString();
        }
        if (this instanceof CmsUint8Array) {
            return uint8ArrayToString();
        }
        return scalarToString();
    }

    private String scalarToString() {
        long val = 0;
        switch (nativeSize) {
            case 1: val = nativePtr.getByte(0); break;
            case 2: val = nativePtr.getShort(0); break;
            case 4: val = nativePtr.getInt(0); break;
            case 8: val = nativePtr.getLong(0); break;
        }
        return "(" + getClass().getSimpleName() + ") " + val;
    }

    private String uint8ArrayToString() {
        CmsUint8Array arr = (CmsUint8Array) this;
        String prefix = "(" + getClass().getSimpleName() + ") ";
        byte[] data = arr.value();
        // Try to show as string
        if (arr.len > 0 && data.length > 0) {
            String s = new String(data, StandardCharsets.UTF_8);
            if (isPrintable(s)) {
                return prefix + "'" + s + "'";
            }
            return prefix + "hex:" + bytesToHex(data);
        }
        return prefix + "(empty)";
    }

    private static boolean isPrintable(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 0x20 && c != '\t' && c != '\n' && c != '\r') return false;
            if (c == 0xFFFD) return false;
        }
        return true;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }

    private static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder(s.length() * count);
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }

    public void zero() {
        if (nativePtr != null) {
            nativePtr.clear((long) nativeSize);
        }
    }
}
