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

    /** postDecode() 时从 Pointer 拷出的缓存，避免 Pointer 指向的内存失效。 */
    private transient byte[] _cachedBytes;

    public static class ByValue extends CmsUint8Array implements Structure.ByValue {}

    public CmsUint8Array() {
        super(false);
    }

    /**
     * 供别名子类使用（如 CmsEntryId、CmsObjectName），启用 codec 后自动绑定子类的 FFI encode/decode。
     */
    protected CmsUint8Array(boolean codecEnabled) {
        super(codecEnabled);
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

    /** 获取字节数组。 */
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
