package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASN.1 BIT STRING type — APER codec.
 *
 * <p>ASN.1 type: <b>BIT STRING</b> (ITU-T X.680 §22)
 * <br>Encoding rules: ITU-T X.691 §16
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>TriggerConditions CODEDENUM — fixed 6-bit (Table 7)</li>
 *   <li>ReasonCode CODEDENUM — fixed 7-bit (7.6.3)</li>
 *   <li>RCBOptFlds — fixed 10-bit (Table 16, report control block optional fields)</li>
 *   <li>MSVCBOptFlds — fixed 5-bit (Table 17)</li>
 *   <li>SmpMod CODEDENUM — fixed 2-bit (Table 18)</li>
 *   <li>TimeQuality — fixed-bit (7.2.3)</li>
 *   <li>Packedlist — variable-length bit string (7.2.5)</li>
 * </ul>
 *
 * <h3>Fixed-size SIZE(n):</h3>
 * <ul>
 *   <li>n <= 16: encode n bits directly (no alignment)</li>
 *   <li>17 <= n <= 65536: align, then encode bytes</li>
 *   <li>n > 65536: encode length, align, then encode bytes</li>
 * </ul>
 *
 * <h3>Variable-length SIZE(lb..ub):</h3>
 * <ul>
 *   <li>Encode actual bit length (constrained integer), align, then encode bit content</li>
 * </ul>
 *
 * <h3>Unconstrained:</h3>
 * <ul>
 *   <li>Encode byte length, align, content bytes + trailing unused-bits count</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Fixed 6-bit: TriggerConditions ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerBitString.encodeFixedSize(pos, 0b000110, 6);
 *   byte[] data = pos.toByteArray();
 *
 *   PerInputStream pis = new PerInputStream(data);
 *   long trigger = PerBitString.decodeFixedSize(pis, 6);  // 6
 *
 *   // --- Fixed 10-bit: RCBOptFlds ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   PerBitString.encodeFixedSize(pos2, 0b0000001011, 10);
 *   byte[] data2 = pos2.toByteArray();
 *
 *   PerInputStream pis2 = new PerInputStream(data2);
 *   long opts = PerBitString.decodeFixedSize(pis2, 10);  // 11
 *
 *   // --- Variable-length constrained ---
 *   PerOutputStream pos3 = new PerOutputStream();
 *   byte[] bits = new byte[]{(byte) 0b10101010};
 *   PerBitString.encodeConstrained(pos3, bits, 8, 0, 65535);
 *   byte[] data3 = pos3.toByteArray();
 *
 *   PerInputStream pis3 = new PerInputStream(data3);
 *   byte[] result = PerBitString.decodeConstrained(pis3, 0, 65535);
 *
 *   // --- Unconstrained ---
 *   PerOutputStream pos4 = new PerOutputStream();
 *   byte[] payload = new byte[]{(byte) 0xAB, (byte) 0xCD};
 *   PerBitString.encodeUnconstrained(pos4, payload, 13);  // 13 valid bits
 *   byte[] data4 = pos4.toByteArray();
 *
 *   PerInputStream pis4 = new PerInputStream(data4);
 *   BitStringResult br = PerBitString.decodeUnconstrained(pis4);
 *   // br.data = [0xAB, 0xCD], br.bitLength = 13
 * }</pre>
 */
public final class PerBitString {

    private PerBitString() { /* utility class */ }

    // ==================== Fixed-size ====================

    /**
     * Encodes a fixed-size bit string as long (for <= 64 bits).
     *
     * @param pos       output stream
     * @param value     bit string value (only low fixedSize bits used)
     * @param fixedSize fixed bit length
     */
    public static void encodeFixedSize(PerOutputStream pos, long value, int fixedSize) {
        if (fixedSize == 0) return;

        if (fixedSize <= 16) {
            pos.writeBits(value, fixedSize);
        } else if (fixedSize <= 65536) {
            pos.align();
            int bytesToWrite = (fixedSize + 7) / 8;
            for (int i = bytesToWrite - 1; i >= 0; i--) {
                byte b = (byte) ((value >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        } else {
            PerInteger.encodeLength(pos, (fixedSize + 7) / 8);
            pos.align();
            int bytesToWrite = (fixedSize + 7) / 8;
            for (int i = bytesToWrite - 1; i >= 0; i--) {
                byte b = (byte) ((value >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        }
    }

    /**
     * Decodes a fixed-size bit string as long (for <= 64 bits).
     *
     * @param pis       input stream
     * @param fixedSize fixed bit length
     * @return bit string value (stored in low bits of long)
     * @throws PerDecodeException if insufficient data
     */
    public static long decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return 0;

        if (fixedSize <= 16) {
            return pis.readBits(fixedSize);
        } else if (fixedSize <= 65536) {
            pis.align();
            int bytesToRead = (fixedSize + 7) / 8;
            return readBytesAsLong(pis, bytesToRead);
        } else {
            int length = PerInteger.decodeLength(pis);
            pis.align();
            return readBytesAsLong(pis, length);
        }
    }

    /**
     * Encodes a fixed-size bit string from byte array (for > 64 bits).
     *
     * @param pos       output stream
     * @param data      bit data as byte array
     * @param totalBits total number of bits
     */
    public static void encodeFixedSize(PerOutputStream pos, byte[] data, int totalBits) {
        if (totalBits == 0 || (data != null && data.length == 0)) return;

        if (totalBits <= 16) {
            long value = bytesToLongBits(data, totalBits);
            pos.writeBits(value, totalBits);
        } else {
            pos.align();
            int bytesNeeded = (totalBits + 7) / 8;
            for (int i = 0; i < bytesNeeded && i < data.length; i++) {
                pos.writeByteAligned(data[i]);
            }
            for (int i = data.length; i < bytesNeeded; i++) {
                pos.writeByteAligned((byte) 0);
            }
        }
    }

    /**
     * Decodes a fixed-size bit string into byte array.
     *
     * @param pis       input stream
     * @param totalBits total number of bits
     * @return byte array containing bit data
     * @throws PerDecodeException if insufficient data
     */
    public static byte[] decodeFixedSizeBytes(PerInputStream pis, int totalBits) throws PerDecodeException {
        if (totalBits == 0) return new byte[0];

        if (totalBits <= 16) {
            long value = pis.readBits(totalBits);
            return longBitsToBytes(value, totalBits);
        }

        pis.align();
        int bytesNeeded = (totalBits + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = 0; i < bytesNeeded; i++) {
            result[i] = (byte) pis.readByteAligned();
        }
        return result;
    }

    // ==================== Variable-size (constrained range) ====================

    /**
     * Encodes a variable-size bit string with SIZE constraint (lb..ub).
     *
     * @param pos         output stream
     * @param data        bit data as byte array
     * @param actualBits  actual number of valid bits
     * @param lowerBound  minimum bit count
     * @param upperBound  maximum bit count
     */
    public static void encodeConstrained(
            PerOutputStream pos, byte[] data, int actualBits,
            int lowerBound, int upperBound) {

        PerInteger.encode(pos, actualBits, lowerBound, upperBound);

        int bytesToWrite = (actualBits + 7) / 8;
        pos.align();
        for (int i = 0; i < bytesToWrite && i < data.length; i++) {
            pos.writeByteAligned(data[i]);
        }
        for (int i = data.length; i < bytesToWrite; i++) {
            pos.writeByteAligned((byte) 0);
        }
    }

    /**
     * Decodes a variable-size bit string with SIZE constraint (lb..ub).
     *
     * @param pos         (not used, kept for API symmetry)
     * @param pis         input stream
     * @param lowerBound  minimum bit count
     * @param upperBound  maximum bit count
     * @return bit data as byte array
     * @throws PerDecodeException if insufficient data
     */
    public static byte[] decodeConstrained(PerInputStream pis, int lowerBound, int upperBound)
            throws PerDecodeException {

        int actualBits = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (actualBits == 0) return new byte[0];

        pis.align();
        int bytesToRead = (actualBits + 7) / 8;
        byte[] result = new byte[bytesToRead];
        for (int i = 0; i < bytesToRead; i++) {
            result[i] = (byte) pis.readByteAligned();
        }
        return result;
    }

    // ==================== Unconstrained ====================

    /**
     * Encodes an unconstrained bit string (format: [byte-length][content][unused-bits-count]).
     *
     * @param pos       output stream
     * @param data      bit data
     * @param totalBits number of valid bits
     */
    public static void encodeUnconstrained(PerOutputStream pos, byte[] data, int totalBits) {
        int contentBytes = (totalBits + 7) / 8;
        int unusedBits = contentBytes * 8 - totalBits;

        // Total length = data bytes + 1 byte unused-bits count
        PerInteger.encodeLength(pos, contentBytes + 1);
        pos.align();

        for (int i = 0; i < contentBytes && (data != null && i < data.length); i++) {
            pos.writeByteAligned(data[i]);
        }
        for (int i = (data != null ? Math.min(data.length, contentBytes) : 0); i < contentBytes; i++) {
            pos.writeByteAligned((byte) 0);
        }

        pos.writeByteAligned((byte) unusedBits);
    }

    /**
     * Decodes an unconstrained bit string.
     *
     * @param pis input stream
     * @return result containing data and valid bit count
     * @throws PerDecodeException if insufficient data
     */
    public static BitStringResult decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int totalLength = PerInteger.decodeLength(pis);
        pis.align();

        byte[] raw = new byte[totalLength];
        for (int i = 0; i < totalLength; i++) {
            raw[i] = (byte) pis.readByteAligned();
        }

        int unusedBits = raw[totalLength - 1] & 0xFF;
        int contentBytes = totalLength - 1;
        int totalBits = contentBytes * 8 - unusedBits;

        byte[] data = new byte[contentBytes];
        System.arraycopy(raw, 0, data, 0, contentBytes);

        return new BitStringResult(data, totalBits);
    }

    // ==================== Internal utilities ====================

    private static long readBytesAsLong(PerInputStream pis, int bytesToRead) throws PerDecodeException {
        long result = 0;
        for (int i = 0; i < bytesToRead; i++) {
            result = (result << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return result;
    }

    private static long bytesToLongBits(byte[] data, int totalBits) {
        long result = 0;
        int bytesToUse = (totalBits + 7) / 8;
        for (int i = 0; i < bytesToUse && i < data.length; i++) {
            result = (result << 8) | (data[i] & 0xFFL);
        }
        if (totalBits % 8 != 0) {
            long mask = (1L << totalBits) - 1;
            result &= mask;
        }
        return result;
    }

    private static byte[] longBitsToBytes(long value, int totalBits) {
        int bytesNeeded = (totalBits + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = bytesNeeded - 1; i >= 0; i--) {
            result[bytesNeeded - 1 - i] = (byte) ((value >> (i * 8)) & 0xFF);
        }
        return result;
    }

    // ==================== Result container ====================

    /** Decode result for unconstrained BIT STRING. */
    public static class BitStringResult {
        /** Bit data (content bytes, excluding unused-bits byte). */
        public final byte[] data;
        /** Number of valid bits. */
        public final int bitLength;

        public BitStringResult(byte[] data, int bitLength) {
            this.data = data;
            this.bitLength = bitLength;
        }
    }
}
