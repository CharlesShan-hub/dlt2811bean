package com.ysh.jcms.per.types;

import com.ysh.jcms.per.exception.PerDecodeException;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ASN.1 VisibleString / IA5String type — APER codec.
 *
 * <p>Synchronized with ccms {@code cms_string.c} PER implementation.
 *
 * <p>Encoding: each character is 8 bits (ISO 8859-1 / ISO 646, ASCII-compatible).
 */
public final class PerVisibleString {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;

    private PerVisibleString() { /* utility class */ }

    // ==================== Fixed-size ====================

    public static void encodeFixedSize(PerOutputStream pos, String value, int fixedSize) {
        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        if (fixedSize * 8 >= 16) pos.align();
        for (int i = 0; i < fixedSize; i++) {
            int ch = (i < bytes.length) ? (bytes[i] & 0xFF) : 0;
            pos.writeBits(ch, 8);
        }
    }

    public static String decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return "";
        if (fixedSize * 8 >= 16) pis.align();
        byte[] bytes = new byte[fixedSize];
        for (int i = 0; i < fixedSize; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET).trim();
    }

    // ==================== Variable-size (constrained range) ====================

    public static void encodeConstrained(PerOutputStream pos, String value,
                                         int lowerBound, int upperBound) {
        int length = value != null ? value.length() : 0;
        PerInteger.encode(pos, length, lowerBound, upperBound);
        if (upperBound * 8 >= 16) pos.align();
        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        for (byte b : bytes) {
            pos.writeBits(b & 0xFF, 8);
        }
    }

    public static String decodeConstrained(PerInputStream pis,
                                           int lowerBound, int upperBound) throws PerDecodeException {
        int length = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (length == 0) return "";
        if (upperBound * 8 >= 16) pis.align();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET);
    }

    // ==================== Unconstrained ====================

    public static void encodeUnconstrained(PerOutputStream pos, String value) {
        byte[] bytes = (value != null && !value.isEmpty())
            ? value.getBytes(DEFAULT_CHARSET) : new byte[0];
        int len = bytes.length;
        PerInteger.encodeLength(pos, len);
        pos.align();
        for (byte b : bytes) {
            pos.writeBits(b & 0xFF, 8);
        }
    }

    public static String decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return "";
        pis.align();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET);
    }
}
