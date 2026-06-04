package com.ysh.jcms.per.types;

import com.ysh.jcms.per.exception.PerDecodeException;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

/**
 * ASN.1 BIT STRING type — APER codec.
 *
 * <p>Synchronized with ccms {@code cms_string.c} PER implementation.
 *
 * <p>All content bits are written/read bit-by-bit (not byte-aligned).
 * Alignment only happens before content when bit count > 16.
 */
public final class PerBitString {

    private PerBitString() { /* utility class */ }

    // ==================== Fixed-size (byte[] version) ====================

    /**
     * Encodes fixed-size bit string from byte array.
     * ≤16 bits: no alignment. >16 bits: align, then bit-by-bit.
     */
    public static void encodeFixedSize(PerOutputStream pos, byte[] data, int totalBits) {
        if (totalBits == 0) return;
        if (totalBits > 16) pos.align();
        for (int i = 0; i < totalBits; i++) {
            int bit = (data[i / 8] >> (7 - (i % 8))) & 1;
            pos.writeBit(bit != 0);
        }
    }

    /**
     * Decodes fixed-size bit string into byte array.
     */
    public static byte[] decodeFixedSizeBytes(PerInputStream pis, int totalBits) throws PerDecodeException {
        if (totalBits == 0) return new byte[0];
        if (totalBits > 16) pis.align();
        int nbytes = (totalBits + 7) / 8;
        byte[] out = new byte[nbytes];
        for (int i = 0; i < totalBits; i++) {
            boolean bit = pis.readBit();
            if (bit) out[i / 8] |= (byte) (0x80 >> (i % 8));
        }
        return out;
    }

    // ==================== Fixed-size (long convenience, ≤64 bits) ====================

    public static void encodeFixedSize(PerOutputStream pos, long value, int fixedSize) {
        byte[] data = longToBytesMsbFirst(value, fixedSize);
        encodeFixedSize(pos, data, fixedSize);
    }

    public static long decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        byte[] data = decodeFixedSizeBytes(pis, fixedSize);
        return bytesToLongMsbFirst(data, fixedSize);
    }

    // ==================== Variable-size (constrained range) ====================

    /**
     * [constrained int bitCount][align if bitCount > 16][bit-by-bit content]
     */
    public static void encodeConstrained(
            PerOutputStream pos, byte[] data, int actualBits,
            int lowerBound, int upperBound) {

        PerInteger.encode(pos, actualBits, lowerBound, upperBound);
        if (actualBits > 16) pos.align();
        for (int i = 0; i < actualBits; i++) {
            int bit = (data[i / 8] >> (7 - (i % 8))) & 1;
            pos.writeBit(bit != 0);
        }
    }

    public static byte[] decodeConstrained(PerInputStream pis, int lowerBound, int upperBound)
            throws PerDecodeException {

        int actualBits = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (actualBits == 0) return new byte[0];
        if (actualBits > 16) pis.align();
        int nbytes = (actualBits + 7) / 8;
        byte[] out = new byte[nbytes];
        for (int i = 0; i < actualBits; i++) {
            boolean bit = pis.readBit();
            if (bit) out[i / 8] |= (byte) (0x80 >> (i % 8));
        }
        return out;
    }

    // ==================== Unconstrained ====================

    /**
     * [semi-constrained nbits][align][bit-by-bit content]
     *
     * Semi-constrained nbits is encoded as: length-prefixed big-endian bytes of (nbits).
     */
    public static void encodeUnconstrained(PerOutputStream pos, byte[] data, int totalBits) {
        PerInteger.encodeSemiConstrained(pos, totalBits, 0);
        if (totalBits > 16) pos.align();
        for (int i = 0; i < totalBits; i++) {
            int bit = (data[i / 8] >> (7 - (i % 8))) & 1;
            pos.writeBit(bit != 0);
        }
    }

    public static BitStringResult decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int totalBits = (int) PerInteger.decodeSemiConstrained(pis, 0);
        if (totalBits == 0) return new BitStringResult(new byte[0], 0);
        if (totalBits > 16) pis.align();
        int nbytes = (totalBits + 7) / 8;
        byte[] out = new byte[nbytes];
        for (int i = 0; i < totalBits; i++) {
            boolean bit = pis.readBit();
            if (bit) out[i / 8] |= (byte) (0x80 >> (i % 8));
        }
        return new BitStringResult(out, totalBits);
    }

    // ==================== Internal utilities ====================

    /** Convert long (LSB-0, value in low bits) to MSB-first byte array. */
    private static byte[] longToBytesMsbFirst(long value, int totalBits) {
        int nbytes = (totalBits + 7) / 8;
        byte[] bytes = new byte[nbytes];
        for (int i = 0; i < nbytes; i++) {
            bytes[i] = (byte) ((value >> (8 * (nbytes - 1 - i))) & 0xFF);
        }
        return bytes;
    }

    /** Convert MSB-first byte array back to long (LSB-0, value in low bits). */
    private static long bytesToLongMsbFirst(byte[] data, int totalBits) {
        int nbytes = (totalBits + 7) / 8;
        long result = 0;
        for (int i = 0; i < nbytes; i++) {
            result = (result << 8) | (data[i] & 0xFFL);
        }
        if (totalBits % 8 != 0) {
            result &= (1L << totalBits) - 1;
        }
        return result;
    }

    // ==================== Result container ====================

    public static class BitStringResult {
        public final byte[] data;
        public final int bitLength;

        public BitStringResult(byte[] data, int bitLength) {
            this.data = data;
            this.bitLength = bitLength;
        }
    }
}
