package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * UTF8StringVar — maps to C {@code cms_utf8_string_var_t}.
 */
public class CmsUTF8StringVar extends CmsStructure {
    public byte[] data;
    public int len;

    public CmsUTF8StringVar() { this.data = new byte[1]; }
    public CmsUTF8StringVar(int structSize) { this.data = new byte[structSize]; }
    public CmsUTF8StringVar(int structSize, String value) {
        this.data = new byte[structSize];
        set(value);
    }

    public String get() {
        int l = 0;
        while (l < data.length && data[l] != 0) l++;
        return new String(data, 0, l, StandardCharsets.UTF_8);
    }
    public void set(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        Arrays.fill(data, (byte) 0);
        len = bytes.length;
        System.arraycopy(bytes, 0, data, 0, Math.min(len, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data", "len"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_utf8_string_var_encode(this, data.length - 1, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_utf8_string_var_decode(data, data.length, this.data.length - 1, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsUTF8StringVar from(byte[] data, int maxLen) { return new CmsUTF8StringVar(maxLen + 1).decode(data); }
    @Override public String toString() { return get(); }
}
