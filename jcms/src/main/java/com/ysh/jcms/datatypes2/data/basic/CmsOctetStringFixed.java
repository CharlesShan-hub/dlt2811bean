package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * OctetStringFixed — maps to C {@code cms_octet_string_fixed_t}.
 */
public class CmsOctetStringFixed extends CmsStructure {
    public byte[] data;

    public CmsOctetStringFixed() { this.data = new byte[1]; }
    public CmsOctetStringFixed(int structSize) { this.data = new byte[structSize]; }
    public CmsOctetStringFixed(int structSize, byte[] value) {
        this.data = new byte[structSize];
        set(value);
    }

    public byte[] get() { return data; }
    public void set(byte[] value) {
        Arrays.fill(data, (byte) 0);
        System.arraycopy(value, 0, data, 0, Math.min(value.length, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_octet_string_fixed_encode(this, data.length, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_octet_string_fixed_decode(data, data.length, this.data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsOctetStringFixed from(byte[] data, int fixedLen) { return new CmsOctetStringFixed(fixedLen).decode(data); }
}
