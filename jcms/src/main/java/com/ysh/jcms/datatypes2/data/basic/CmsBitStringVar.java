package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * BitStringVar — maps to C {@code cms_bit_string_var_t}.
 * Variable number of bits, constrained length prefix in PER.
 */
public class CmsBitStringVar extends CmsStructure {
    public byte[] data;
    public int nbits;

    public CmsBitStringVar() { this.data = new byte[1]; this.nbits = 0; }
    public CmsBitStringVar(int maxNbits) { this.data = new byte[(maxNbits + 7) / 8]; this.nbits = 0; }
    public CmsBitStringVar(int maxNbits, byte[] value, int nbits) {
        this.data = new byte[(maxNbits + 7) / 8];
        this.nbits = nbits;
        set(value);
    }

    public byte[] get() { return data; }
    public void set(byte[] value) {
        System.arraycopy(value, 0, data, 0, Math.min(value.length, data.length));
    }

    @Override protected List<String> getFieldOrder() { return Arrays.asList("data", "nbits"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_bit_string_var_encode(this, 8 * data.length, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_bit_string_var_decode(data, data.length, 8 * this.data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsBitStringVar from(byte[] data, int maxNbits) { return new CmsBitStringVar(maxNbits).decode(data); }
}
