package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsString {

    private final String value;

    public CmsString(String value) {
        this.value = value;
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
        IntByReference strLen = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_VisibleString(data, data.length, strBuf, strLen);
        return new CmsString(new String(strBuf, 0, strLen.getValue()));
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
        IntByReference strLen = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_UTF8String(data, data.length, strBuf, strLen);
        return new CmsString(new String(strBuf, 0, strLen.getValue()));
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
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_ObjectName(data, data.length, strBuf, strLen);
        return new CmsString(new String(strBuf, 0, strLen.getValue()));
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
        IntByReference strLen = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_ObjectReference(data, data.length, strBuf, strLen);
        return new CmsString(new String(strBuf, 0, strLen.getValue()));
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
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(strBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_SubReference(data, data.length, strBuf, strLen);
        return new CmsString(new String(strBuf, 0, strLen.getValue()));
    }
}
