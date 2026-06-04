package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * OctetStringVar — maps to C {@code cms_octet_string_var_t}.
 */
public class CmsOctetStringVar extends CmsStructure {
    public byte[] data;
    public int len;

    public CmsOctetStringVar() { this.data = new byte[1]; }
    public CmsOctetStringVar(int structSize) { this.data = new byte[structSize]; }
    public CmsOctetStringVar(int structSize, byte[] value) {
        this.data = new byte[structSize];
        set(value);
    }

    public byte[] get() { return data; }
    public void set(byte[] value) {
        Arrays.fill(data, (byte) 0);
        len = value.length;
        System.arraycopy(value, 0, data, 0, Math.min(len, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data", "len"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_octet_string_var_encode(this, data.length - 1, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_octet_string_var_decode(data, data.length, this.data.length - 1, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsOctetStringVar from(byte[] data, int maxLen) { return new CmsOctetStringVar(maxLen + 1).decode(data); }
}
