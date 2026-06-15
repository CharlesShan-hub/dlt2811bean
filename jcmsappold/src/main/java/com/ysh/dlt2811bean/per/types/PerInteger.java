package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

import java.io.ByteArrayOutputStream;

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
            return;
        }

        long offset = value - lowerBound;

        if (range < 256) {
            int bitsNeeded = calculateBitsNeeded(range);
            pos.writeBits(offset, bitsNeeded);
        } else if (range <= 65536) {
            int bytesNeeded = calculateBytesForRange(range);
            pos.align();
            for (int i = bytesNeeded - 1; i >= 0; i--) {
                byte b = (byte) ((offset >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        } else {
            byte[] content = encodeUnsignedValueToBytes(offset);
            int maxLen = calculateBytesForRange(range);
            encode(pos, content.length, 1, maxLen);
            pos.align();
            pos.writeBytes(content);
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

        if (range < 256) {
            int bitsNeeded = calculateBitsNeeded(range);
            long offset = pis.readBits(bitsNeeded);
            return lowerBound + offset;
        } else if (range <= 65536) {
            int bytesNeeded = calculateBytesForRange(range);
            pis.align();
            long offset = 0;
            for (int i = 0; i < bytesNeeded; i++) {
                offset = (offset << 8) | (pis.readByteAligned() & 0xFFL);
            }
            return lowerBound + offset;
        } else {
            int maxLen = calculateBytesForRange(range);
            int contentLen = (int) decode(pis, 1, maxLen);
            pis.align();
            byte[] content = pis.readBytes(contentLen);
            long offset = bytesToUnsignedLong(content);
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
     * <br>>>=16384: throws — use {@link #encodeContent} for fragmented encoding.
     *
     * @param pos    output stream
     * @param length length value (0..16383)
     */
    public static void encodeLength(PerOutputStream pos, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be >= 0, got " + length);
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
            throw new IllegalArgumentException(
                "Length " + length + " >= 16384 requires fragmented encoding — use encodeContent()");
        }
    }

    /**
     * Decodes a length field per APER rules.
     *
     * @param pis input stream
     * @return decoded length value (0..16383)
     * @throws PerDecodeException if fragmented form (11xxxxxx) is encountered — use {@link #decodeContent}
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

        throw new PerDecodeException(
            "Encountered fragmented length header (11xxxxxx) — use decodeContent() for length >= 16384");
    }

    // ==================== Content encoding with fragmentation (§11.9.3.8) ====================

    /**
     * Encodes content bytes with PER length handling, including 16K+ fragmentation.
     *
     * <p>For length &lt; 16384: writes short/medium form length + content bytes.
     * <br>For length >= 16384: writes fragment headers and content chunks iteratively.
     *
     * @param pos  output stream
     * @param data content bytes
     */
    public static void encodeContent(PerOutputStream pos, byte[] data) {
        encodeContent(pos, data, 0, data != null ? data.length : 0);
    }

    /**
     * Encodes content bytes with PER length handling, including 16K+ fragmentation.
     *
     * @param pos    output stream
     * @param data   content bytes
     * @param offset start offset in data
     * @param length number of bytes to encode
     */
    public static void encodeContent(PerOutputStream pos, byte[] data, int offset, int length) {
        if (length == 0) {
            encodeLength(pos, 0);
            return;
        }

        int remaining = length;
        int dataOff = offset;

        while (remaining > 0) {
            if (remaining <= 127) {
                pos.writeByteAligned((byte) remaining);
                pos.writeBytes(data, dataOff, remaining);
                break;
            } else if (remaining <= 16383) {
                byte high = (byte) ((remaining >> 8) | 0x80);
                byte low = (byte) (remaining & 0xFF);
                pos.writeByteAligned(high);
                pos.writeByteAligned(low);
                pos.writeBytes(data, dataOff, remaining);
                break;
            } else {
                int k = Math.min(4, remaining / 16384);
                int chunk = k * 16384;
                byte header = (byte) (0xC0 | (k & 0x3F));
                pos.writeByteAligned(header);
                pos.writeBytes(data, dataOff, chunk);
                remaining -= chunk;
                dataOff += chunk;
            }
        }

        if (remaining == 0 && length > 0 && length % 16384 == 0) {
            pos.writeByteAligned((byte) 0);
        }
    }

    /**
     * Decodes content bytes with PER length handling, including 16K+ fragmentation.
     *
     * @param pis input stream
     * @return decoded content bytes
     * @throws PerDecodeException if insufficient data or encoding error
     */
    public static byte[] decodeContent(PerInputStream pis) throws PerDecodeException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while (true) {
            pis.align();
            if (!pis.hasRemaining()) break;

            int firstByte = pis.readByteAligned() & 0xFF;

            if ((firstByte & 0x80) == 0) {
                if (firstByte == 0) break;
                baos.write(pis.readBytes(firstByte), 0, firstByte);
                break;
            } else if ((firstByte & 0xC0) == 0x80) {
                int secondByte = pis.readByteAligned() & 0xFF;
                int chunkLen = ((firstByte & 0x3F) << 8) | secondByte;
                if (chunkLen > 0) {
                    baos.write(pis.readBytes(chunkLen), 0, chunkLen);
                }
                break;
            } else {
                int k = firstByte & 0x3F;
                int chunkLen = k * 16384;
                baos.write(pis.readBytes(chunkLen), 0, chunkLen);
            }
        }

        return baos.toByteArray();
    }

    // ==================== Internal utilities ====================

    /** Calculates bits needed to represent (range-1), i.e. ceil(log2(range)). */
    static int calculateBitsNeeded(long range) {
        if (range <= 1) return 0;
        return Long.SIZE - Long.numberOfLeadingZeros(range - 1);
    }

    /** Calculates bytes needed for large range offsets. */
    static int calculateBytesForRange(long range) {
        int bits = calculateBitsNeeded(range);
        return (bits + 7) / 8;
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
