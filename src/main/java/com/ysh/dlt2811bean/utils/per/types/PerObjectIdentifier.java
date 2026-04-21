package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * ASN.1 OBJECT IDENTIFIER type — APER codec.
 *
 * <p>ASN.1 type: <b>OBJECT IDENTIFIER</b> (ITU-T X.680 §31)
 * <br>Encoding rules: ITU-T X.691 §23
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>Not directly used as service parameter in the standard body text</li>
 *   <li>Commonly used in MMS protocol layer for domain identifiers, type identifiers, etc.</li>
 *   <li>May be needed for IEC 61850 interoperability scenarios</li>
 * </ul>
 *
 * <p>Encoding:
 * <ul>
 *   <li>OID encoded as a byte sequence of sub-identifiers</li>
 *   <li>First two sub-identifiers (a, b) merged into first byte: 40*a + b</li>
 *   <li>Subsequent sub-identifiers use BER-style variable-length encoding
 *       (7 bits per group, MSB as continuation flag)</li>
 *   <li>Unconstrained format: length + content bytes</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Encode OID: 1.3.6.1 (iso.org.dod.internet) ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerObjectIdentifier.encode(pos, new int[]{1, 3, 6, 1});
 *   byte[] data = pos.toByteArray();
 *
 *   // --- Decode OID ---
 *   PerInputStream pis = new PerInputStream(data);
 *   int[] oid = PerObjectIdentifier.decode(pis);  // {1, 3, 6, 1}
 *
 *   // --- String conversion ---
 *   String dotted = PerObjectIdentifier.toString(oid);        // "1.3.6.1"
 *   int[] parsed = PerObjectIdentifier.fromString("1.3.6.1"); // {1, 3, 6, 1}
 * }</pre>
 */
public final class PerObjectIdentifier {

    private PerObjectIdentifier() { /* utility class */ }

    /**
     * Encodes an OBJECT IDENTIFIER.
     *
     * @param pos        output stream
     * @param components OID sub-identifier array (e.g. {1, 3, 6, 1})
     * @throws IllegalArgumentException if components are invalid
     */
    public static void encode(PerOutputStream pos, int[] components) {
        if (components == null || components.length == 0) {
            PerInteger.encodeLength(pos, 0);
            return;
        }

        byte[] content = encodeComponents(components);
        PerInteger.encodeLength(pos, content.length);
        pos.writeBytes(content);
    }

    /**
     * Decodes an OBJECT IDENTIFIER.
     *
     * @param pis input stream
     * @return OID sub-identifier array
     * @throws PerDecodeException if insufficient data or format error
     */
    public static int[] decode(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) {
            return new int[0];
        }

        byte[] content = pis.readBytes(length);
        return decodeComponents(content);
    }

    /**
     * Converts an OID array to dotted-decimal string representation.
     *
     * @param components OID sub-identifier array
     * @return dotted string, e.g. "1.3.6.1"
     */
    public static String toString(int[] components) {
        if (components == null || components.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            if (i > 0) sb.append('.');
            sb.append(components[i]);
        }
        return sb.toString();
    }

    /**
     * Parses an OID array from dotted-decimal string.
     *
     * @param oidStr dotted string, e.g. "1.3.6.1"
     * @return OID sub-identifier array
     * @throws IllegalArgumentException if format is invalid
     */
    public static int[] fromString(String oidStr) {
        if (oidStr == null || oidStr.isEmpty()) {
            return new int[0];
        }
        String[] parts = oidStr.split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    // ==================== Internal ====================

    /** Encodes OID sub-identifiers into BER-style byte sequence. */
    private static byte[] encodeComponents(int[] components) {
        byte[] buf = new byte[components.length * 5 + 1];
        int pos = 0;

        if (components.length >= 1) {
            int a = components[0];
            int b = components.length >= 2 ? components[1] : 0;
            if (a > 2 || (a == 2 && b > 39)) {
                throw new IllegalArgumentException(
                        "Invalid OID root: first component must be 0, 1, or 2; got " + a);
            }
            buf[pos++] = (byte) (40 * a + b);
        }

        for (int i = 2; i < components.length; i++) {
            pos = encodeSubIdentifier(components[i], buf, pos);
        }

        byte[] result = new byte[pos];
        System.arraycopy(buf, 0, result, 0, pos);
        return result;
    }

    /** Encodes a single sub-identifier using BER variable-length 7-bit encoding. */
    private static int encodeSubIdentifier(int value, byte[] buf, int pos) {
        if (value < 0x80) {
            buf[pos++] = (byte) value;
        } else if (value < 0x4000) {
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        } else if (value < 0x200000) {
            buf[pos++] = (byte) (0x80 | ((value >> 14) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        } else if (value < 0x10000000) {
            buf[pos++] = (byte) (0x80 | ((value >> 21) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 14) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        } else {
            buf[pos++] = (byte) (0x80 | ((value >> 28) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 21) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 14) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        }
        return pos;
    }

    /** Decodes OID sub-identifiers from BER-style byte sequence. */
    private static int[] decodeComponents(byte[] content) throws PerDecodeException {
        int[] estimate = new int[content.length + 1];
        int count = 0;

        if (content.length == 0) {
            return new int[0];
        }

        // First byte encodes 40*a + b
        int first = content[0] & 0xFF;
        int a = first / 40;
        int b = first % 40;
        // Special case: a=2 allows b > 39
        if (a > 2) {
            b = first - 80;
            a = 2;
        }
        estimate[count++] = a;
        estimate[count++] = b;

        int bytePos = 1;
        while (bytePos < content.length) {
            long subId = 0;
            while (bytePos < content.length) {
                int b2 = content[bytePos++] & 0xFF;
                subId = (subId << 7) | (b2 & 0x7F);
                if ((b2 & 0x80) == 0) break;
            }
            if (subId > Integer.MAX_VALUE) {
                throw new PerDecodeException("OID sub-identifier too large: " + subId);
            }
            estimate[count++] = (int) subId;
        }

        int[] result = new int[count];
        System.arraycopy(estimate, 0, result, 0, count);
        return result;
    }
}