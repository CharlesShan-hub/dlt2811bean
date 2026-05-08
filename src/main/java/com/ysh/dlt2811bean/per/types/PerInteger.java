package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASN.1 INTEGER type — APER codec.
 *
 * <p>ASN.1 type: <b>INTEGER</b> (ITU-T X.680 §21)
 * <br>Encoding rules: ITU-T X.691 §12
 *
 * <p>This class provides the core PER encoding primitives.
 * For 2811-specific typed integers (INT8U, INT16U, etc.), see {@code CmsInt8U}, {@code CmsInt16U} etc. in the {@code data} package.
 *
 * <h3>Constrained INTEGER (lb..ub):</h3>
 * <ul>
 *   <li>range=1 (d=1): 0 bits — both sides know the only possible value</li>
 *   <li>2..255: ceil(log2(range)) bits — compact encoding</li>
 *   <li>256..65536: 2 bytes after alignment</li>
 *   <li>>65536: ceil(log2(range)/8) bytes after alignment</li>
 * </ul>
 *
 * <h3>Semi-constrained INTEGER (lb..MAX):</h3>
 * <ul>
 *   <li>Format: [length L][content bytes]</li>
 * </ul>
 *
 * <h3>Unconstrained INTEGER:</h3>
 * <ul>
 *   <li>Format: [length L][content bytes], two's complement form</li>
 * </ul>
 *
 * <h3>Normally small non-negative integer:</h3>
 * <ul>
 *   <li>0..63: 1-bit flag (0) + 6-bit value = 7 bits total</li>
 *   <li>>=64: 1-bit flag (1) + semi-constrained encoding</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Generic constrained encode ---
 *   PerInteger.encode(pos, 300, 0, 65535);
 *   long val = PerInteger.decode(pis, 0, 65535);
 *
 *   // --- Unconstrained: signed value ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   PerInteger.encodeUnconstrained(pos2, -100);
 *
 *   PerInputStream pis2 = new PerInputStream(pos2.toByteArray());
 *   long unconstrained = PerInteger.decodeUnconstrained(pis2);  // -100
 *
 *   // --- Normally small non-negative (CHOICE index, etc.) ---
 *   PerOutputStream pos3 = new PerOutputStream();
 *   PerInteger.encodeSmallNonNegative(pos3, 5);   // 7 bits
 *
 *   PerInputStream pis3 = new PerInputStream(pos3.toByteArray());
 *   long idx = PerInteger.decodeSmallNonNegative(pis3);  // 5
 * }</pre>
 */
public final class PerInteger {

    private PerInteger() { /* utility class */ }

    // ==================== Constrained INTEGER ====================

    /**
     * Encodes a constrained integer value (lb..ub).
     *
     * @param pos         output stream
     * @param value       integer value (must be in [lowerBound, upperBound])
     * @param lowerBound  lower bound
     * @param upperBound  upper bound
     * @throws IllegalArgumentException if value is out of range
     */
    public static void encode(PerOutputStream pos, long value, long lowerBound, long upperBound) {
        if (value < lowerBound || value > upperBound) {
            throw new IllegalArgumentException(
                String.format("Value %d out of constrained range [%d, %d]", value, lowerBound, upperBound));
        }

        long range = upperBound - lowerBound + 1;
        if (range == 1) {
            // Only one possible value — encode nothing
            return;
        }

        if (range <= 256) {
            int bitsNeeded = calculateBitsNeeded(range);
            long offset = value - lowerBound;
            pos.writeBits(offset, bitsNeeded);
        } else if (range <= 65536) {
            long offset = value - lowerBound;
            pos.align();
            pos.writeBits(offset, 16);
        } else {
            long offset = value - lowerBound;
            int bytesNeeded = calculateBytesForRange(range);
            pos.align();
            for (int i = bytesNeeded - 1; i >= 0; i--) {
                byte b = (byte) ((offset >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        }
    }

    /**
     * Decodes a constrained integer value (lb..ub).
     *
     * @param pis         input stream
     * @param lowerBound  lower bound
     * @param upperBound  upper bound
     * @return decoded integer value
     * @throws PerDecodeException if insufficient data
     */
    public static long decode(PerInputStream pis, long lowerBound, long upperBound) throws PerDecodeException {
        long range = upperBound - lowerBound + 1;
        if (range == 1) {
            return lowerBound;
        }

        if (range <= 256) {
            int bitsNeeded = calculateBitsNeeded(range);
            long offset = pis.readBits(bitsNeeded);
            return lowerBound + offset;
        } else if (range <= 65536) {
            pis.align();
            long offset = pis.readBits(16);
            return lowerBound + offset;
        } else {
            int bytesNeeded = calculateBytesForRange(range);
            pis.align();
            long offset = 0;
            for (int i = 0; i < bytesNeeded; i++) {
                offset = (offset << 8) | (pis.readByteAligned() & 0xFFL);
            }
            return lowerBound + offset;
        }
    }

    // ==================== Normally small non-negative integer ====================

    /**
     * Encodes a normally small non-negative integer (X.691 §11.6).
     *
     * <p>0..63: 1-bit flag (0) + 6-bit value (7 bits total).
     * <br>>=64: 1-bit flag (1) + semi-constrained encoding.
     *
     * @param pos   output stream
     * @param value non-negative integer value
     */
    public static void encodeSmallNonNegative(PerOutputStream pos, int value) {
        if (value >= 0 && value <= 63) {
            pos.writeBit(false);
            pos.writeBits(value, 6);
        } else {
            pos.writeBit(true);
            encodeSemiConstrained(pos, (long) value, 0);
        }
    }

    /**
     * Decodes a normally small non-negative integer.
     *
     * @param pis input stream
     * @return decoded non-negative integer value
     * @throws PerDecodeException if insufficient data
     */
    public static long decodeSmallNonNegative(PerInputStream pis) throws PerDecodeException {
        boolean isLarge = pis.readBit();
        if (!isLarge) {
            return pis.readBits(6);
        } else {
            return decodeSemiConstrained(pis, 0);
        }
    }

    // ==================== Semi-constrained / Unconstrained INTEGER ====================

    /**
     * Encodes a semi-constrained integer (lb..MAX).
     *
     * <p>Format: [length L][content bytes].
     *
     * @param pos         output stream
     * @param value       integer value
     * @param lowerBound  lower bound (use 0 for pure unconstrained)
     */
    public static void encodeSemiConstrained(PerOutputStream pos, long value, long lowerBound) {
        long offset = value - lowerBound;
        byte[] content = encodeUnsignedValueToBytes(offset);
        encodeLength(pos, content.length);
        pos.writeBytes(content);
    }

    /**
     * Decodes a semi-constrained integer (lb..MAX).
     *
     * @param pis         input stream
     * @param lowerBound  lower bound
     * @return decoded integer value
     * @throws PerDecodeException if insufficient data
     */
    public static long decodeSemiConstrained(PerInputStream pis, long lowerBound) throws PerDecodeException {
        int length = decodeLength(pis);
        byte[] content = pis.readBytes(length);
        long offset = bytesToUnsignedLong(content);
        return lowerBound + offset;
    }

    /**
     * Encodes an unconstrained integer (signed, two's complement form).
     *
     * <p>Format: [length L][content bytes].
     *
     * @param pos   output stream
     * @param value integer value (positive or negative)
     */
    public static void encodeUnconstrained(PerOutputStream pos, long value) {
        byte[] content = encodeSignedValueToBytes(value);
        encodeLength(pos, content.length);
        pos.writeBytes(content);
    }

    /**
     * Decodes an unconstrained integer (signed).
     *
     * @param pis input stream
     * @return decoded integer value
     * @throws PerDecodeException if insufficient data
     */
    public static long decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = decodeLength(pis);
        byte[] content = pis.readBytes(length);
        return bytesToSignedLong(content);
    }

    // ==================== Length determinant (APER rules) ====================

    /**
     * Encodes a length field per APER rules (X.691 §11.9).
     *
     * <p>0..127: 1 byte, MSB=0, lower 7 bits = length.
     * <br>128..16383: 2 bytes, MSB=10, 14 bits = length.
     * <br>>=16384: multi-fragment, MSB=11.
     *
     * @param pos    output stream
     * @param length length value (>= 0)
     */
    public static void encodeLength(PerOutputStream pos, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be >= 0");
        }

        if (length <= 127) {
            // Short form: 0xxxxxxx
            pos.writeByteAligned((byte) length);
        } else if (length <= 16383) {
            // Medium form: 10xxxxxxxxxxxxxx
            byte high = (byte) ((length >> 8) | 0x80);
            byte low = (byte) (length & 0xFF);
            pos.writeByteAligned(high);
            pos.writeByteAligned(low);
        } else {
            // Long form: 11 + (fragments - 1) in low 6 bits
            int fragments = (length + 16383) / 16384;
            byte header = (byte) (((fragments - 1) & 0x3F) | 0xC0);
            pos.writeByteAligned(header);

            int remaining = length;
            for (int i = 0; i < fragments; i++) {
                int segmentLen = Math.min(remaining, 16384);
                pos.writeByteAligned((byte) (segmentLen >> 8));
                pos.writeByteAligned((byte) (segmentLen & 0xFF));
                remaining -= segmentLen;
            }
        }
    }

    /**
     * Decodes a length field per APER rules.
     *
     * @param pis input stream
     * @return decoded length value
     * @throws PerDecodeException if insufficient data
     */
    public static int decodeLength(PerInputStream pis) throws PerDecodeException {
        pis.align();

        int firstByte = pis.readByteAligned() & 0xFF;

        if ((firstByte & 0x80) == 0) {
            // Short form: 0xxxxxxx (value 0..127)
            return firstByte;
        }

        if ((firstByte & 0xC0) == 0x80) {
            // Medium form: 10xxxxxxxxxxxxxx (value 128..16383)
            int secondByte = pis.readByteAligned() & 0xFF;
            return ((firstByte & 0x3F) << 8) | secondByte;
        }

        // Long form: 11 + (fragments - 1) in low 6 bits
        int numFragments = (firstByte & 0x3F) + 1;
        int totalLength = 0;
        for (int i = 0; i < numFragments; i++) {
            int hi = pis.readByteAligned() & 0xFF;
            int lo = pis.readByteAligned() & 0xFF;
            totalLength = totalLength + ((hi << 8) | lo);
        }
        return totalLength;
    }

    // ==================== Internal utilities ====================

    /** Calculates bits needed to represent (range-1), i.e. ceil(log2(range)). */
    static int calculateBitsNeeded(long range) {
        if (range <= 1) return 0;
        return Long.SIZE - Long.numberOfLeadingZeros(range - 1);
    }

    /** Calculates bytes needed for large range offsets. */
    static int calculateBytesForRange(long range) {
        if (range <= 65536) return 2;
        long maxOffset = range - 1;
        if (maxOffset <= 0xFFFFFFL) return 3;
        if (maxOffset <= 0xFFFFFFFFL) return 4;
        if (maxOffset <= 0xFFFFFFFFFFL) return 5;
        if (maxOffset <= 0xFFFFFFFFFFFFL) return 6;
        if (maxOffset <= 0xFFFFFFFFFFFFFFL) return 7;
        return 8;
    }

    /** Encodes an unsigned long to minimal big-endian bytes. */
    static byte[] encodeUnsignedValueToBytes(long value) {
        if (value == 0) return new byte[]{0};
        int bytesNeeded = (Long.SIZE - Long.numberOfLeadingZeros(value) + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = bytesNeeded - 1; i >= 0; i--) {
            result[bytesNeeded - 1 - i] = (byte) ((value >> (i * 8)) & 0xFF);
        }
        return result;
    }

    /** Encodes a signed long to BER-compatible minimal two's complement big-endian bytes. */
    static byte[] encodeSignedValueToBytes(long value) {
        if (value == 0) return new byte[]{0};

        int bytesNeeded;
        if (value > 0) {
            bytesNeeded = (Long.SIZE - Long.numberOfLeadingZeros(value) + 8) / 8;
        } else {
            bytesNeeded = (Long.SIZE - Long.numberOfLeadingZeros(~value) + 8) / 8;
        }

        byte[] result = new byte[bytesNeeded];
        for (int i = 0; i < bytesNeeded; i++) {
            int shift = (bytesNeeded - 1 - i) * 8;
            result[i] = (byte) ((value >> shift) & 0xFF);
        }
        return result;
    }

    /** Reads a big-endian byte array as an unsigned long. */
    static long bytesToUnsignedLong(byte[] data) {
        long result = 0;
        for (byte b : data) {
            result = (result << 8) | (b & 0xFFL);
        }
        return result;
    }

    /** Reads a big-endian byte array as a signed long (two's complement). */
    static long bytesToSignedLong(byte[] data) {
        if (data.length == 0) return 0;
        long result = data[0] & 0xFFL;
        if ((data[0] & 0x80) != 0) {
            result |= (~0L) << 8;
        }
        for (int i = 1; i < data.length; i++) {
            result = (result << 8) | (data[i] & 0xFFL);
        }
        return result;
    }
}
