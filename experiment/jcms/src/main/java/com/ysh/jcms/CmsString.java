package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;

@EqualsAndHashCode
public final class CmsString {

    private final String value;

    public CmsString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.value = value;
    }

    public String get() {
        return value;
    }

    public byte[] getBytes() {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] encodeVisibleString() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_VisibleString(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsString decodeVisibleString(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference cap = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_VisibleString(data, data.length, strBuf, cap);
        return new CmsString(new String(strBuf, 0, cap.getValue(), StandardCharsets.US_ASCII));
    }

    public byte[] encodeUTF8String() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_UTF8String(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsString decodeUTF8String(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference cap = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_UTF8String(data, data.length, strBuf, cap);
        return new CmsString(new String(strBuf, 0, cap.getValue(), StandardCharsets.UTF_8));
    }

    public byte[] encodeObjectName() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_ObjectName(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsString decodeObjectName(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference cap = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_ObjectName(data, data.length, strBuf, cap);
        return new CmsString(new String(strBuf, 0, cap.getValue(), StandardCharsets.US_ASCII));
    }

    public byte[] encodeObjectReference() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_ObjectReference(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsString decodeObjectReference(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference cap = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_ObjectReference(data, data.length, strBuf, cap);
        return new CmsString(new String(strBuf, 0, cap.getValue(), StandardCharsets.US_ASCII));
    }

    public byte[] encodeSubReference() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_SubReference(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsString decodeSubReference(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference cap = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_SubReference(data, data.length, strBuf, cap);
        return new CmsString(new String(strBuf, 0, cap.getValue(), StandardCharsets.US_ASCII));
    }

    @Override
    public String toString() {
        return value;
    }
}
