package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class CmsFloat {

    private final double value;

    public CmsFloat(double value) {
        this.value = value;
    }

    public double get() {
        return value;
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
        float[] v = new float[1];
        CmsFFI.INSTANCE.cms_ffi_decode_Float32(data, data.length, v);
        return new CmsFloat(v[0]);
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
        double[] v = new double[1];
        CmsFFI.INSTANCE.cms_ffi_decode_Float64(data, data.length, v);
        return new CmsFloat(v[0]);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
