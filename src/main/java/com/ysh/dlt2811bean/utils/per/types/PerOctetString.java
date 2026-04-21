package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

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

        pos.align();

        int writeLen = Math.min(data != null ? data.length : 0, fixedSize);
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
        int length = (data != null) ? data.length : 0;
        PerInteger.encodeLength(pos, length);
        if (length > 0) {
            pos.writeBytes(data.clone());
        }
    }

    /**
     * Decodes a semi-constrained / unconstrained octet string.
     *
     * @param pis input stream
     * @return decoded byte array
     * @throws PerDecodeException if insufficient data
     */
    public static byte[] decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return new byte[0];
        return pis.readBytes(length);
    }
}
