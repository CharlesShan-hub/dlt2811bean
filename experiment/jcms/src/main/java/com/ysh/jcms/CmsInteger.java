package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class CmsInteger {

    private final long value;

    public CmsInteger(long value) {
        this.value = value;
    }

    public long get() {
        return value;
    }

    public int intValue() {
        return (int) value;
    }

    public byte[] encodeInt8() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int8((byte) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt8(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int8(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt8U() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int8U((short) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt8U(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int8U(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt16() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int16((short) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt16(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int16(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt16U() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int16U((int) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt16U(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int16U(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt32() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int32((int) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt32(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int32(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt32U() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int32U(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt32U(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int32U(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt64() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int64(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt64(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int64(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    public byte[] encodeInt64U() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Int64U(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInteger decodeInt64U(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Int64U(data, data.length, v);
        return new CmsInteger(v.getValue());
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
