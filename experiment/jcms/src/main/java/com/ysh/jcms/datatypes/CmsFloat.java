package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsFloat {

    private final double value;

    public CmsFloat(double value) {
        this.value = value;
    }

    public float floatValue() {
        return (float) value;
    }

    public byte[] encodeFloat32() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Float32((float) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFloat decodeFloat32(byte[] data) {
        FloatByReference v = new FloatByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Float32(data, data.length, v);
        return new CmsFloat(v.getValue());
    }

    public byte[] encodeFloat64() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Float64(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFloat decodeFloat64(byte[] data) {
        com.sun.jna.ptr.DoubleByReference v = new com.sun.jna.ptr.DoubleByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Float64(data, data.length, v);
        return new CmsFloat(v.getValue());
    }
}
