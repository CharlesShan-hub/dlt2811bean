package com.ysh.jcms.core;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * jcms2 FFI — ccms2 动态库桥接。
 *
 * 参数全用 Pointer，Java 侧只管写字段到 native 内存，
 * 然后传指针给 C 做 PER 编解码。
 */
public class NativeBridge {

    private interface Lib extends Library {
        int cms_boolean_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_boolean_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int8_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int8u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8u_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int16_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int16_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int16u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int16u_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int24u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int24u_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int32_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int32_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int32u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int32u_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int64_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int64_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_int64u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int64u_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_float32_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_float32_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_float64_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_float64_decode(Pointer v, byte[] inBuf, int inLen);

        int cms_enumerated_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_enumerated_decode(Pointer v, byte[] inBuf, int inLen);
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

    public static byte[] encodeBoolean(Pointer p) { return encode(p, LIB::cms_boolean_encode); }
    public static void decodeBoolean(Pointer p, byte[] d) { decode(p, d, LIB::cms_boolean_decode); }

    public static byte[] encodeInt8(Pointer p) { return encode(p, LIB::cms_int8_encode); }
    public static void decodeInt8(Pointer p, byte[] d) { decode(p, d, LIB::cms_int8_decode); }

    public static byte[] encodeInt8U(Pointer p) { return encode(p, LIB::cms_int8u_encode); }
    public static void decodeInt8U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int8u_decode); }

    public static byte[] encodeInt16(Pointer p) { return encode(p, LIB::cms_int16_encode); }
    public static void decodeInt16(Pointer p, byte[] d) { decode(p, d, LIB::cms_int16_decode); }

    public static byte[] encodeInt16U(Pointer p) { return encode(p, LIB::cms_int16u_encode); }
    public static void decodeInt16U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int16u_decode); }

    public static byte[] encodeInt24U(Pointer p) { return encode(p, LIB::cms_int24u_encode); }
    public static void decodeInt24U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int24u_decode); }

    public static byte[] encodeInt32(Pointer p) { return encode(p, LIB::cms_int32_encode); }
    public static void decodeInt32(Pointer p, byte[] d) { decode(p, d, LIB::cms_int32_decode); }

    public static byte[] encodeInt32U(Pointer p) { return encode(p, LIB::cms_int32u_encode); }
    public static void decodeInt32U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int32u_decode); }

    public static byte[] encodeInt64(Pointer p) { return encode(p, LIB::cms_int64_encode); }
    public static void decodeInt64(Pointer p, byte[] d) { decode(p, d, LIB::cms_int64_decode); }

    public static byte[] encodeInt64U(Pointer p) { return encode(p, LIB::cms_int64u_encode); }
    public static void decodeInt64U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int64u_decode); }

    public static byte[] encodeFloat32(Pointer p) { return encode(p, LIB::cms_float32_encode); }
    public static void decodeFloat32(Pointer p, byte[] d) { decode(p, d, LIB::cms_float32_decode); }

    public static byte[] encodeFloat64(Pointer p) { return encode(p, LIB::cms_float64_encode); }
    public static void decodeFloat64(Pointer p, byte[] d) { decode(p, d, LIB::cms_float64_decode); }

    public static byte[] encodeEnumerated(Pointer p) { return encode(p, LIB::cms_enumerated_encode); }
    public static void decodeEnumerated(Pointer p, byte[] d) { decode(p, d, LIB::cms_enumerated_decode); }
}
