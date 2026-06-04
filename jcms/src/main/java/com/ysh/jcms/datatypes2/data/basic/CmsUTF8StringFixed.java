package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * UTF8StringFixed — maps to C {@code cms_utf8_string_fixed_t}.
 */
public class CmsUTF8StringFixed extends CmsStructure {
    public byte[] data;

    public CmsUTF8StringFixed() { this.data = new byte[1]; }
    public CmsUTF8StringFixed(int structSize) { this.data = new byte[structSize]; }
    public CmsUTF8StringFixed(int structSize, String value) {
        this.data = new byte[structSize];
        set(value);
    }

    public String get() {
        int len = 0;
        while (len < data.length && data[len] != 0) len++;
        return new String(data, 0, len, StandardCharsets.UTF_8);
    }
    public void set(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        Arrays.fill(data, (byte) 0);
        System.arraycopy(bytes, 0, data, 0, Math.min(bytes.length, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_utf8_string_fixed_encode(this, data.length, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_utf8_string_fixed_decode(data, data.length, this.data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsUTF8StringFixed from(byte[] data, int fixedLen) { return new CmsUTF8StringFixed(fixedLen).decode(data); }
    @Override public String toString() { return get(); }
}
