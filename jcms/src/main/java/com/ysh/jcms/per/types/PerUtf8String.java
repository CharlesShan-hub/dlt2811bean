package com.ysh.jcms.per.types;

import com.ysh.jcms.per.exception.PerDecodeException;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

import java.nio.charset.StandardCharsets;

/**
 * ASN.1 UTF8String type — APER codec.
 *
 * <p>Synchronized with ccms {@code cms_string.c} PER implementation.
 */
public final class PerUtf8String {

    private PerUtf8String() { /* utility class */ }

    // ==================== Fixed-size ====================

    public static void encodeFixedSize(PerOutputStream pos, String value, int fixedLen) {
        byte[] bytes = value != null ? value.getBytes(StandardCharsets.UTF_8) : new byte[0];
        int len = Math.min(bytes.length, fixedLen);
        pos.align();
        pos.writeBytes(bytes, 0, len);
        for (int i = len; i < fixedLen; i++) {
            pos.writeByteAligned((byte) 0);
        }
    }

    public static String decodeFixedSize(PerInputStream pis, int fixedLen) throws PerDecodeException {
        if (fixedLen == 0) return "";
        pis.align();
        byte[] bytes = pis.readBytes(fixedLen);
        return new String(bytes, StandardCharsets.UTF_8).trim();
    }

    // ==================== Variable-size (constrained range) ====================

    public static void encodeConstrained(PerOutputStream pos, String value,
                                         int lowerBound, int upperBound) {
        byte[] bytes = value != null ? value.getBytes(StandardCharsets.UTF_8) : new byte[0];
        int actualLength = bytes.length;
        if (actualLength < lowerBound || actualLength > upperBound) {
            throw new IllegalArgumentException(
                String.format("UTF8String byte length %d out of range [%d, %d]",
                    actualLength, lowerBound, upperBound));
        }
        PerInteger.encode(pos, actualLength, lowerBound, upperBound);
        pos.writeBytes(bytes);
    }

    public static String decodeConstrained(PerInputStream pis,
                                           int lowerBound, int upperBound) throws PerDecodeException {
        int length = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (length == 0) return "";
        byte[] bytes = pis.readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // ==================== Unconstrained ====================

    public static void encodeUnconstrained(PerOutputStream pos, String value) {
        byte[] bytes = (value != null && !value.isEmpty())
            ? value.getBytes(StandardCharsets.UTF_8) : new byte[0];
        PerInteger.encodeLength(pos, bytes.length);
        pos.writeBytes(bytes);
    }

    public static String decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return "";
        byte[] bytes = pis.readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
