package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * BitStringFixed — maps to C {@code cms_bit_string_fixed_t}.
 * Fixed number of bits, no length prefix in PER.
 */
public class CmsBitStringFixed extends CmsStructure {
    public byte[] data;
    public int nbits;

    public CmsBitStringFixed() { this.data = new byte[1]; this.nbits = 0; }
    public CmsBitStringFixed(int nbits) { this.data = new byte[(nbits + 7) / 8]; this.nbits = nbits; }
    public CmsBitStringFixed(int nbits, byte[] value) {
        this.data = new byte[(nbits + 7) / 8];
        this.nbits = nbits;
        set(value);
    }

    public byte[] get() { return data; }
    public void set(byte[] value) {
        System.arraycopy(value, 0, data, 0, Math.min(value.length, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data", "nbits"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_bit_string_fixed_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_bit_string_fixed_decode(data, data.length, nbits, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsBitStringFixed from(byte[] data, int nbits) { return new CmsBitStringFixed(nbits).decode(data); }
}
