package com.ysh.jcms.datatype.basic;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.ffi.CmsType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CmsUint8Array extends CmsType {
    public Pointer value;
    public int len;

    /** Cached bytes extracted from native Pointer after decode. */
    private transient byte[] _cachedBytes;

    /** Default constructor — value is null, for embedding without codec. */
    public CmsUint8Array() {
        super(false);
    }

    /**
     * Pre-allocate a buffer of maxLen bytes in native memory.
     *
     * @param maxLen buffer size ({@code > 0} allocates {@code new Memory(maxLen)})
     */
    public CmsUint8Array(int maxLen) {
        this(maxLen, false);
    }

    /**
     * For alias subclasses (CmsEntryId, CmsObjectName, etc.) that need both
     * a pre-allocated buffer and FFI codec.
     *
     * @param maxLen       buffer size ({@code > 0} allocates {@code new Memory(maxLen)})
     * @param codecEnabled whether to bind FFI encode/decode
     */
    protected CmsUint8Array(int maxLen, boolean codecEnabled) {
        super(codecEnabled);
        this.value = maxLen > 0 ? new Memory(maxLen) : null;
        this.len = 0;
    }

    public static class ByValue extends CmsUint8Array implements Structure.ByValue {
        public ByValue() { this(0); }
        public ByValue(int maxLen) { super(maxLen); }
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("value", "len");
    }

    @Override
    protected void postDecode() {
        if (value != null && len > 0) {
            _cachedBytes = value.getByteArray(0, len);
        } else {
            _cachedBytes = new byte[0];
        }
    }

    /** Get the underlying byte array, cached from native memory. */
    public byte[] bytes() {
        if (_cachedBytes != null) return _cachedBytes;
        if (value == null || len == 0) return new byte[0];
        return value.getByteArray(0, len);
    }

    public CmsUint8Array bytes(byte[] data) {
        this._cachedBytes = null;
        this.value = new Memory(data.length);
        this.value.write(0, data, 0, data.length);
        this.len = data.length;
        return this;
    }

    public CmsUint8Array bytes(char[] data) {
        return bytes(new String(data).getBytes(StandardCharsets.UTF_8));
    }

    public CmsUint8Array bytes(String data) {
        return bytes(data.getBytes(StandardCharsets.UTF_8));
    }
}
