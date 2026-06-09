package com.ysh.jcms2.nativebridge;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * jcms2 FFI — 参数用 Pointer，不涉及 JNA Structure。
 *
 * Java 侧只管把字段写到 native 内存，然后调 C 函数编码。
 * 解码时 C 函数把 native 内存填好，Java 侧读字段即可。
 */
public class NativeBridge {

    private interface Lib extends Library {
        // ---- Boolean ----
        int cms_boolean_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_boolean_decode(Pointer v, byte[] inBuf, int inLen);

        // ---- Int8 (signed) ----
        int cms_int8_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8_decode(Pointer v, byte[] inBuf, int inLen);

        // ---- Int8U (unsigned) ----
        int cms_int8u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8u_decode(Pointer v, byte[] inBuf, int inLen);

        // ---- ObjectName (VisibleString SIZE(0..64)) ----
        int cms_object_name_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_object_name_decode(Pointer v, byte[] inBuf, int inLen);
    }

    private static final Lib LIB = Native.load("ccms", Lib.class);

    private static byte[] encode(Pointer structPtr, Encoder fn) {
        byte[] buf = new byte[64];
        IntByReference outLen = new IntByReference(buf.length);
        int rc = fn.encode(structPtr, buf, outLen);
        if (rc != 0) throw new RuntimeException("encode failed: " + rc);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    private static void decode(Pointer structPtr, byte[] data, Decoder fn) {
        int rc = fn.decode(structPtr, data, data.length);
        if (rc != 0) throw new RuntimeException("decode failed: " + rc);
    }

    @FunctionalInterface private interface Encoder { int encode(Pointer v, byte[] buf, IntByReference outLen); }
    @FunctionalInterface private interface Decoder { int decode(Pointer v, byte[] buf, int len); }

    // ==================== Boolean ====================

    public static byte[] encodeBoolean(Pointer structPtr) { return encode(structPtr, LIB::cms_boolean_encode); }
    public static void decodeBoolean(Pointer structPtr, byte[] data) { decode(structPtr, data, LIB::cms_boolean_decode); }

    // ==================== Int8 ====================

    public static byte[] encodeInt8(Pointer structPtr) { return encode(structPtr, LIB::cms_int8_encode); }
    public static void decodeInt8(Pointer structPtr, byte[] data) { decode(structPtr, data, LIB::cms_int8_decode); }

    // ==================== Int8U ====================

    public static byte[] encodeInt8U(Pointer structPtr) { return encode(structPtr, LIB::cms_int8u_encode); }
    public static void decodeInt8U(Pointer structPtr, byte[] data) { decode(structPtr, data, LIB::cms_int8u_decode); }

    // ==================== ObjectName ====================

    public static byte[] encodeObjectName(Pointer structPtr) { return encode(structPtr, LIB::cms_object_name_encode); }
    public static void decodeObjectName(Pointer structPtr, byte[] data) { decode(structPtr, data, LIB::cms_object_name_decode); }
}
