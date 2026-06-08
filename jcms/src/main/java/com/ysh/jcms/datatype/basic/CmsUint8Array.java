package com.ysh.jcms.datatype.basic;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.ffi.CmsField;
import com.ysh.jcms.ffi.CmsType;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CmsUint8Array extends CmsField {
    public Pointer pointer;
    public int len;

    /** Cached bytes extracted from native Pointer after decode. */
    private transient byte[] _cachedBytes;

    /** Default constructor — null pointer, for embedding without codec. */
    public CmsUint8Array() {
        super();
    }

    /**
     * Pre-allocate a buffer of maxLen bytes in native memory.
     *
     * @param maxLen buffer size ({@code > 0} allocates {@code new Memory(maxLen)})
     */
    public CmsUint8Array(int maxLen) {
        this();
        this.pointer = maxLen > 0 ? new Memory(maxLen + 1) : null;
        this.len = 0;
    }

    /** For alias subclasses that need a pre-allocated buffer. */
    protected CmsUint8Array(int maxLen, boolean codecIgnored) {
        this(maxLen);
    }

    public static class ByValue extends CmsUint8Array implements Structure.ByValue {
        public ByValue() { this(0); }
        public ByValue(int maxLen) { super(maxLen); }
        @Override
        public ByValue value(byte[] data) { return (ByValue) super.value(data); }
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("pointer", "len");
    }

    @Override
    protected void postDecode() {
        if (pointer != null && len > 0) {
            _cachedBytes = pointer.getByteArray(0, len);
        } else {
            _cachedBytes = new byte[0];
        }
    }

    /** Get the underlying byte array, cached from native memory. */
    public byte[] value() {
        if (_cachedBytes != null) return _cachedBytes;
        if (pointer == null || len == 0) return new byte[0];
        return pointer.getByteArray(0, len);
    }

    public CmsUint8Array value(byte[] data) {
        this._cachedBytes = null;
        if (data.length > 0) {
            this.pointer = new Memory(data.length + 1);
            this.pointer.write(0, data, 0, data.length);
            this.pointer.setByte(data.length, (byte) 0);  // null terminate for C str
        } else {
            this.pointer = null;
        }
        this.len = data.length;
        return this;
    }

    public CmsUint8Array value(char[] data) {
        return value(new String(data).getBytes(StandardCharsets.UTF_8));
    }

    public CmsUint8Array value(String data) {
        return value(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsUint8Array)) return false;
        return Arrays.equals(value(), ((CmsUint8Array) o).value());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value());
    }
}
