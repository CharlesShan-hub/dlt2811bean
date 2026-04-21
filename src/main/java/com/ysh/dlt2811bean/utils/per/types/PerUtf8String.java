package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import java.nio.charset.StandardCharsets;

/**
 * ASN.1 UTF8String / BMPString type — APER codec.
 *
 * <p>ASN.1 type: <b>UTF8String</b> (ITU-T X.680 §42) / <b>BMPString</b> (X.680 §41)
 * <br>Encoding rules: ITU-T X.691 §42
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>UNICODE STRING — Unicode string data value (7.1.5, Table 6 data string types)</li>
 *   <li>Rarely used in actual service parameters; mainly for non-ASCII device names or descriptions</li>
 * </ul>
 *
 * <p>Encoding:
 * <ul>
 *   <li>UTF8String: convert to UTF-8 bytes, then encode as OCTET STRING</li>
 *   <li>BMPString: each character is 2 bytes (UCS-2 big-endian), encode as OCTET STRING</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- UTF-8 unconstrained ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerUtf8String.encodeUtf8(pos, "device name");
 *   byte[] data = pos.toByteArray();
 *
 *   PerInputStream pis = new PerInputStream(data);
 *   String name = PerUtf8String.decodeUtf8(pis);  // "device name"
 *
 *   // --- UTF-8 constrained ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   PerUtf8String.encodeUtf8Constrained(pos2, "description text", 0, 255);
 *   byte[] data2 = pos2.toByteArray();
 *
 *   PerInputStream pis2 = new PerInputStream(data2);
 *   String desc = PerUtf8String.decodeUtf8Constrained(pis2, 0, 255);
 *
 *   // --- BMP fixed-size ---
 *   PerOutputStream pos3 = new PerOutputStream();
 *   PerUtf8String.encodeBmpFixedSize(pos3, "AB", 2);
 *   byte[] data3 = pos3.toByteArray();
 *
 *   PerInputStream pis3 = new PerInputStream(data3);
 *   String bmp = PerUtf8String.decodeBmpFixedSize(pis3, 2);  // "AB"
 * }</pre>
 */
public final class PerUtf8String {

    private PerUtf8String() { /* utility class */ }

    // ==================== UTF8String ====================

    /**
     * Encodes UTF8String (unconstrained).
     *
     * @param pos   output stream
     * @param value string value
     */
    public static void encodeUtf8(PerOutputStream pos, String value) {
        if (value == null || value.isEmpty()) {
            PerInteger.encodeLength(pos, 0);
            return;
        }

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        PerInteger.encodeLength(pos, bytes.length);
        pos.writeBytes(bytes);
    }

    /**
     * Decodes UTF8String (unconstrained).
     *
     * @param pis input stream
     * @return decoded string
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeUtf8(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return "";

        byte[] bytes = pis.readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Encodes UTF8String with SIZE constraint (lb..ub).
     *
     * <p>Note: the constraint applies to UTF-8 encoded byte count, not character count.
     *
     * @param pos         output stream
     * @param value       string value
     * @param lowerBound  minimum byte count
     * @param upperBound  maximum byte count
     * @throws IllegalArgumentException if UTF-8 byte count is out of range
     */
    public static void encodeUtf8Constrained(PerOutputStream pos, String value,
                                             int lowerBound, int upperBound) {
        byte[] bytes = value != null ? value.getBytes(StandardCharsets.UTF_8) : new byte[0];
        int actualLength = bytes.length;

        if (actualLength < lowerBound || actualLength > upperBound) {
            throw new IllegalArgumentException(
                    String.format("UTF8String byte length %d out of range [%d, %d]",
                            actualLength, lowerBound, upperBound));
        }

        PerInteger.encode(pos, actualLength, lowerBound, upperBound);
        if (actualLength > 0) {
            pos.writeBytes(bytes);
        }
    }

    /**
     * Decodes UTF8String with SIZE constraint (lb..ub).
     *
     * @param pis         input stream
     * @param lowerBound  minimum byte count
     * @param upperBound  maximum byte count
     * @return decoded string
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeUtf8Constrained(PerInputStream pis,
                                               int lowerBound, int upperBound) throws PerDecodeException {
        int length = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (length == 0) return "";

        byte[] bytes = pis.readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // ==================== BMPString (UCS-2, 2 bytes per character) ====================

    /**
     * Encodes BMPString (fixed-size, UCS-2 big-endian, 2 bytes per character).
     *
     * @param pos            output stream
     * @param value          string value
     * @param fixedCharCount fixed character count
     */
    public static void encodeBmpFixedSize(PerOutputStream pos, String value, int fixedCharCount) {
        pos.align();
        for (int i = 0; i < fixedCharCount; i++) {
            char ch = (i < value.length()) ? value.charAt(i) : ' ';
            pos.writeByteAligned((byte) (ch >> 8));
            pos.writeByteAligned((byte) (ch & 0xFF));
        }
    }

    /**
     * Decodes BMPString (fixed-size).
     *
     * @param pis            input stream
     * @param fixedCharCount fixed character count
     * @return decoded string (trimmed)
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeBmpFixedSize(PerInputStream pis, int fixedCharCount)
            throws PerDecodeException {
        if (fixedCharCount == 0) return "";

        pis.align();
        byte[] bytes = pis.readBytes(fixedCharCount * 2);
        StringBuilder sb = new StringBuilder(fixedCharCount);
        for (int i = 0; i < fixedCharCount * 2; i += 2) {
            char ch = (char) (((bytes[i] & 0xFF) << 8) | (bytes[i + 1] & 0xFF));
            sb.append(ch);
        }
        return sb.toString().trim();
    }

    /**
     * Encodes BMPString (variable-size).
     *
     * @param pos         output stream
     * @param value       string value
     * @param lowerBound  minimum character count
     * @param upperBound  maximum character count
     */
    public static void encodeBmpConstrained(PerOutputStream pos, String value,
                                            int lowerBound, int upperBound) {
        int charCount = value != null ? value.length() : 0;
        PerInteger.encode(pos, charCount, lowerBound, upperBound);

        pos.align();
        for (int i = 0; i < charCount; i++) {
            char ch = value.charAt(i);
            pos.writeByteAligned((byte) (ch >> 8));
            pos.writeByteAligned((byte) (ch & 0xFF));
        }
    }

    /**
     * Decodes BMPString (variable-size).
     *
     * @param pis         input stream
     * @param lowerBound  minimum character count
     * @param upperBound  maximum character count
     * @return decoded string
     * @throws PerDecodeException if insufficient data
     */
    public static String decodeBmpConstrained(PerInputStream pis,
                                              int lowerBound, int upperBound) throws PerDecodeException {
        int charCount = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (charCount == 0) return "";

        pis.align();
        byte[] bytes = pis.readBytes(charCount * 2);
        StringBuilder sb = new StringBuilder(charCount);
        for (int i = 0; i < charCount * 2; i += 2) {
            char ch = (char) (((bytes[i] & 0xFF) << 8) | (bytes[i + 1] & 0xFF));
            sb.append(ch);
        }
        return sb.toString();
    }
}