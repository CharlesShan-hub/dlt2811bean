package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASN.1 OCTET STRING type — APER codec.
 *
 * <p>ASN.1 type: <b>OCTET STRING</b> (ITU-T X.680 §23)
 * <br>Encoding rules: ITU-T X.691 §17
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>OCTET STRING(64) — association identifier (Table 19, associationId)</li>
 *   <li>OCTET STRING — authentication parameter (Table 19)</li>
 *   <li>OCTET STRING — file data (Table 73, fileData)</li>
 *   <li>OCTET STRING — signature certificate / signature value (security parameters)</li>
 * </ul>
 *
 * <h3>Fixed-size SIZE(n):</h3>
 * <ul>
 *   <li>Align, then write n bytes directly. No length field.</li>
 * </ul>
 *
 * <h3>Variable-size SIZE(lb..ub):</h3>
 * <ul>
 *   <li>Encode actual byte length (constrained integer), align, then write content bytes.</li>
 * </ul>
 *
 * <h3>Unconstrained:</h3>
 * <ul>
 *   <li>Encode byte count (length determinant), then write content bytes.</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Fixed 64-byte: associationId ---
 *   PerOutputStream pos = new PerOutputStream();
 *   byte[] assocId = new byte[64];
 *   Arrays.fill(assocId, (byte) 0xAB);
 *   PerOctetString.encodeFixedSize(pos, assocId, 64);
 *   byte[] data = pos.toByteArray();
 *
 *   PerInputStream pis = new PerInputStream(data);
 *   byte[] decoded = PerOctetString.decodeFixedSize(pis, 64);  // 64 bytes of 0xAB
 *
 *   // --- Variable-size constrained: authenticationParameter ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   byte[] cert = new byte[]{0x01, 0x02, 0x03};
 *   PerOctetString.encodeConstrained(pos2, cert, 0, 8192);
 *   byte[] data2 = pos2.toByteArray();
 *
 *   PerInputStream pis2 = new PerInputStream(data2);
 *   byte[] certOut = PerOctetString.decodeConstrained(pis2, 0, 8192);
 *
 *   // --- Unconstrained ---
 *   PerOutputStream pos3 = new PerOutputStream();
 *   byte[] fileData = "Hello DL/T 2811".getBytes(StandardCharsets.UTF_8);
 *   PerOctetString.encodeUnconstrained(pos3, fileData);
 *   byte[] data3 = pos3.toByteArray();
 *
 *   PerInputStream pis3 = new PerInputStream(data3);
 *   byte[] out = PerOctetString.decodeUnconstrained(pis3);
 * }</pre>
 */
public final class PerOctetString {

    private PerOctetString() { /* utility class */ }

    // ==================== Int ⇔ Fixed-size OCTET STRING ====================

    /**
     * Encodes an int as a 2-byte big-endian OCTET STRING (SIZE(2)).
     * Equivalent to {@code encodeFixedSize(pos, new byte[]{(v>>8)&0xFF, v&0xFF}, 2)}.
     *
     * @param pos   output stream
     * @param value int value (only lower 16 bits are used)
     */
    public static void encodeInt2(PerOutputStream pos, int value) {
        pos.align();
        pos.writeByteAligned((byte) ((value >> 8) & 0xFF));
        pos.writeByteAligned((byte) (value & 0xFF));
    }

    /**
     * Decodes a 2-byte big-endian OCTET STRING (SIZE(2)) to int.
     *
     * @param pis input stream
     * @return decoded int value
     * @throws PerDecodeException if insufficient data
     */
    public static int decodeInt2(PerInputStream pis) throws PerDecodeException {
        pis.align();
        byte[] b = pis.readBytes(2);
        return ((b[0] & 0xFF) << 8) | (b[1] & 0xFF);
    }

    /**
     * Encodes an int as a 4-byte big-endian OCTET STRING (SIZE(4)).
     *
     * @param pos   output stream
     * @param value int value
     */
    public static void encodeInt4(PerOutputStream pos, int value) {
        pos.align();
        pos.writeByteAligned((byte) ((value >> 24) & 0xFF));
        pos.writeByteAligned((byte) ((value >> 16) & 0xFF));
        pos.writeByteAligned((byte) ((value >> 8) & 0xFF));
        pos.writeByteAligned((byte) (value & 0xFF));
    }

    /**
     * Decodes a 4-byte big-endian OCTET STRING (SIZE(4)) to int.
     *
     * @param pis input stream
     * @return decoded int value
     * @throws PerDecodeException if insufficient data
     */
    public static int decodeInt4(PerInputStream pis) throws PerDecodeException {
        pis.align();
        byte[] b = pis.readBytes(4);
        return ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16)
             | ((b[2] & 0xFF) << 8) | (b[3] & 0xFF);
    }

    // ==================== UtcTime / GeneralizedTime ====================

    /** UTCTime fixed length: YYMMDDHHMMSSZ (8 ASCII bytes) */
    public static final int UTC_TIME_SIZE = 8;

    /** GeneralizedTime fixed length: YYYYMMDDHHMMSSZ (13 ASCII bytes) */
    public static final int GENERALIZED_TIME_SIZE = 13;

    /**
     * Encodes a UTCTime string (YYMMDDHHMMSSZ) as a fixed 8-byte OCTET STRING.
     *
     * @param pos      output stream
     * @param timeStr  ASCII time string, e.g. "260421093000Z"
     * @throws IllegalArgumentException if timeStr is not exactly 8 bytes
     */
    public static void encodeUtcTime(PerOutputStream pos, String timeStr) {
        byte[] bytes = toAsciiBytes(timeStr, UTC_TIME_SIZE, "UtcTime");
        encodeFixedSize(pos, bytes, UTC_TIME_SIZE);
    }

    /**
     * Decodes a fixed 8-byte OCTET STRING to a UTCTime string.
     *
     * @param pis input stream
     * @return ASCII time string, e.g. "260421093000Z"
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeUtcTime(PerInputStream pis) throws PerDecodeException {
        byte[] bytes = decodeFixedSize(pis, UTC_TIME_SIZE);
        return new String(bytes, java.nio.charset.StandardCharsets.US_ASCII);
    }

    /**
     * Encodes a GeneralizedTime string (YYYYMMDDHHMMSSZ) as a fixed 13-byte OCTET STRING.
     *
     * @param pos      output stream
     * @param timeStr  ASCII time string, e.g. "20260421093000Z"
     * @throws IllegalArgumentException if timeStr is not exactly 13 bytes
     */
    public static void encodeGeneralizedTime(PerOutputStream pos, String timeStr) {
        byte[] bytes = toAsciiBytes(timeStr, GENERALIZED_TIME_SIZE, "GeneralizedTime");
        encodeFixedSize(pos, bytes, GENERALIZED_TIME_SIZE);
    }

    /**
     * Decodes a fixed 13-byte OCTET STRING to a GeneralizedTime string.
     *
     * @param pis input stream
     * @return ASCII time string, e.g. "20260421093000Z"
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeGeneralizedTime(PerInputStream pis) throws PerDecodeException {
        byte[] bytes = decodeFixedSize(pis, GENERALIZED_TIME_SIZE);
        return new String(bytes, java.nio.charset.StandardCharsets.US_ASCII);
    }

    private static byte[] toAsciiBytes(String s, int expectedLen, String typeName) {
        byte[] bytes = (s != null ? s : "").getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        if (bytes.length != expectedLen) {
            throw new IllegalArgumentException(
                typeName + " must be exactly " + expectedLen + " ASCII bytes, got " + bytes.length);
        }
        return bytes;
    }

    // ==================== Fixed-size ====================

    /**
     * Encodes a fixed-size octet string.
     *
     * @param pos       output stream
     * @param data      byte data
     * @param fixedSize fixed byte length
     * @throws IllegalArgumentException if data length does not match fixedSize
     */
    public static void encodeFixedSize(PerOutputStream pos, byte[] data, int fixedSize) {
        if (fixedSize == 0) return;

        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }

        if (fixedSize <= 2) {
            long value = 0;
            int writeLen = Math.min(data.length, fixedSize);
            for (int i = 0; i < writeLen; i++) {
                value = (value << 8) | (data[i] & 0xFFL);
            }
            pos.writeBits(value, fixedSize * 8);
            return;
        }

        pos.align();

        int writeLen = Math.min(data.length, fixedSize);
        for (int i = 0; i < writeLen; i++) {
            pos.writeByteAligned(data[i]);
        }
        for (int i = writeLen; i < fixedSize; i++) {
            pos.writeByteAligned((byte) 0);
        }
    }

    /**
     * Decodes a fixed-size octet string.
     *
     * @param pis       input stream
     * @param fixedSize fixed byte length
     * @return decoded byte array
     * @throws PerDecodeException if insufficient data
     */
    public static byte[] decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return new byte[0];

        if (fixedSize <= 2) {
            long value = pis.readBits(fixedSize * 8);
            byte[] result = new byte[fixedSize];
            for (int i = 0; i < fixedSize; i++) {
                result[i] = (byte) ((value >> ((fixedSize - 1 - i) * 8)) & 0xFF);
            }
            return result;
        }

        pis.align();
        return pis.readBytes(fixedSize);
    }

    // ==================== Variable-size (constrained range) ====================

    /**
     * Encodes a variable-size octet string with SIZE constraint (lb..ub).
     *
     * @param pos         output stream
     * @param data        byte data
     * @param lowerBound  minimum length
     * @param upperBound  maximum length
     * @throws IllegalArgumentException if data length is out of range
     */
    public static void encodeConstrained(PerOutputStream pos, byte[] data,
                                         int lowerBound, int upperBound) {
        int actualLength = (data != null) ? data.length : 0;
        if (actualLength < lowerBound || actualLength > upperBound) {
            throw new IllegalArgumentException(
                String.format("OCTET STRING length %d out of range [%d, %d]",
                    actualLength, lowerBound, upperBound));
        }

        PerInteger.encode(pos, actualLength, lowerBound, upperBound);

        pos.align();
        for (int i = 0; i < actualLength; i++) {
            pos.writeByteAligned(data[i]);
        }
    }

    /**
     * Decodes a variable-size octet string with SIZE constraint (lb..ub).
     *
     * @param pis         input stream
     * @param lowerBound  minimum length
     * @param upperBound  maximum length
     * @return decoded byte array
     * @throws PerDecodeException if insufficient data
     */
    public static byte[] decodeConstrained(PerInputStream pis,
                                           int lowerBound, int upperBound) throws PerDecodeException {
        int actualLength = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (actualLength == 0) return new byte[0];

        pis.align();
        return pis.readBytes(actualLength);
    }

    // ==================== Unconstrained / Semi-constrained ====================

    /**
     * Encodes a semi-constrained / unconstrained octet string.
     *
     * @param pos  output stream
     * @param data byte data
     */
    public static void encodeUnconstrained(PerOutputStream pos, byte[] data) {
        byte[] bytes = (data != null) ? data : new byte[0];
        PerInteger.encodeContent(pos, bytes);
    }

    /**
     * Decodes a semi-constrained / unconstrained octet string.
     *
     * @param pis input stream
     * @return decoded byte array
     * @throws PerDecodeException if insufficient data
     */
    public static byte[] decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        return PerInteger.decodeContent(pis);
    }
}
