package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ASN.1 VisibleString / IA5String type — APER codec.
 *
 * <p>ASN.1 type: <b>VisibleString</b> (ITU-T X.680 §40) / <b>IA5String</b> (X.680 §39)
 * <br>Encoding rules: ITU-T X.691 §41
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>VisibleString(129) — server access point reference (Table 19)</li>
 *   <li>VisibleString(255) — file name (Table 73, fileName)</li>
 *   <li>VisibleString — object reference ObjectReference (7.3.4, LD/LN path)</li>
 *   <li>VISIBLESTRING — method reference (Table 81)</li>
 * </ul>
 *
 * <p>Encoding: each character is 8 bits (ISO 8859-1 / ISO 646, ASCII-compatible).
 * For FROM-constrained strings, each character uses ceil(log2(charsetSize)) bits.
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Fixed 129-char: serverAccessPointReference ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerVisibleString.encodeFixedSize(pos, "S1.AccessPoint1", 129);
 *   byte[] data = pos.toByteArray();
 *
 *   PerInputStream pis = new PerInputStream(data);
 *   String ref = PerVisibleString.decodeFixedSize(pis, 129);  // "S1.AccessPoint1"
 *
 *   // --- Variable-size: ObjectReference ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   PerVisibleString.encodeConstrained(pos2, "LD1/LN0.DO1", 0, 255);
 *   byte[] data2 = pos2.toByteArray();
 *
 *   PerInputStream pis2 = new PerInputStream(data2);
 *   String objRef = PerVisibleString.decodeConstrained(pis2, 0, 255);  // "LD1/LN0.DO1"
 *
 *   // --- Unconstrained ---
 *   PerOutputStream pos3 = new PerOutputStream();
 *   PerVisibleString.encodeUnconstrained(pos3, "any length string");
 *   byte[] data3 = pos3.toByteArray();
 *
 *   PerInputStream pis3 = new PerInputStream(data3);
 *   String s = PerVisibleString.decodeUnconstrained(pis3);
 * }</pre>
 */
public final class PerVisibleString {

    /** Default charset: ISO 8859-1 (VisibleString uses ISO 646, ASCII-compatible). */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;

    private PerVisibleString() { /* utility class */ }

    // ==================== Fixed-size (no charset constraint) ====================

    /**
     * Encodes a fixed-size visible string (8 bits per character, no FROM constraint).
     *
     * <p>Shorter strings are padded with spaces (0x20).
     *
     * @param pos       output stream
     * @param value     string value
     * @param fixedSize fixed character count
     */
    public static void encodeFixedSize(PerOutputStream pos, String value, int fixedSize) {
        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        int writeLen = Math.min(bytes.length, fixedSize);
        for (int i = 0; i < writeLen; i++) {
            pos.writeBits(bytes[i] & 0xFF, 8);
        }
        for (int i = writeLen; i < fixedSize; i++) {
            pos.writeBits(0x20, 8); // space padding
        }
    }

    /**
     * Decodes a fixed-size visible string (no FROM constraint).
     *
     * @param pis       input stream
     * @param fixedSize fixed character count
     * @return decoded string (trailing spaces trimmed)
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return "";
        byte[] bytes = new byte[fixedSize];
        for (int i = 0; i < fixedSize; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET).trim();
    }

    // ==================== Fixed-size (WITH FROM charset constraint) ====================

    /**
     * Encodes a fixed-size visible string with FROM charset constraint.
     *
     * <p>Each character uses ceil(log2(charsetSize)) bits.
     *
     * @param pos          output stream
     * @param value        string value
     * @param fixedSize    fixed character count
     * @param charsetTable allowed character set
     */
    public static void encodeFixedSizeConstrained(
            PerOutputStream pos, String value, int fixedSize, String charsetTable) {

        int bitsPerChar = calculateBitsPerChar(charsetTable.length());

        for (int i = 0; i < fixedSize; i++) {
            char ch = (i < value.length()) ? value.charAt(i) : ' ';
            int index = charsetTable.indexOf(ch);
            if (index < 0) index = charsetTable.indexOf(' ');
            if (index < 0) index = 0;
            pos.writeBits(index, bitsPerChar);
        }
    }

    /**
     * Decodes a fixed-size visible string with FROM charset constraint.
     *
     * @param pis          input stream
     * @param fixedSize    fixed character count
     * @param charsetTable allowed character set
     * @return decoded string (trimmed)
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeFixedSizeConstrained(
            PerInputStream pis, int fixedSize, String charsetTable) throws PerDecodeException {

        if (fixedSize == 0) return "";
        int bitsPerChar = calculateBitsPerChar(charsetTable.length());
        StringBuilder sb = new StringBuilder(fixedSize);

        for (int i = 0; i < fixedSize; i++) {
            long index = pis.readBits(bitsPerChar);
            char ch = (index >= 0 && index < charsetTable.length())
                    ? charsetTable.charAt((int) index)
                    : '?';
            sb.append(ch);
        }
        return sb.toString().trim();
    }

    // ==================== Variable-size ====================

    /**
     * Encodes a variable-size visible string (no FROM constraint).
     *
     * @param pos         output stream
     * @param value       string value
     * @param lowerBound  minimum length
     * @param upperBound  maximum length
     */
    public static void encodeConstrained(PerOutputStream pos, String value,
                                         int lowerBound, int upperBound) {
        int length = value != null ? value.length() : 0;
        PerInteger.encode(pos, length, lowerBound, upperBound);

        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        for (byte b : bytes) {
            pos.writeBits(b & 0xFF, 8);
        }
    }

    /**
     * Decodes a variable-size visible string (no FROM constraint).
     *
     * @param pis         input stream
     * @param lowerBound  minimum length
     * @param upperBound  maximum length
     * @return decoded string
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeConstrained(PerInputStream pis,
                                           int lowerBound, int upperBound) throws PerDecodeException {

        int length = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (length == 0) return "";

        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET);
    }

    /**
     * Encodes an unconstrained / semi-constrained visible string.
     *
     * @param pos   output stream
     * @param value string value
     */
    public static void encodeUnconstrained(PerOutputStream pos, String value) {
        if (value == null || value.isEmpty()) {
            PerInteger.encodeLength(pos, 0);
            return;
        }

        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        PerInteger.encodeLength(pos, bytes.length);
        for (byte b : bytes) {
            pos.writeBits(b & 0xFF, 8);
        }
    }

    /**
     * Decodes an unconstrained / semi-constrained visible string.
     *
     * @param pis input stream
     * @return decoded string
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return "";

        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET);
    }

    // ==================== Internal ====================

    private static int calculateBitsPerChar(int charsetSize) {
        if (charsetSize <= 1) return 0;
        return Integer.SIZE - Integer.numberOfLeadingZeros(charsetSize - 1);
    }
}