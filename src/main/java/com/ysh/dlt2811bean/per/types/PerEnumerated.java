package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASN.1 ENUMERATED type — APER codec.
 *
 * <p>ASN.1 type: <b>ENUMERATED</b> (ITU-T X.680 §20)
 * <br>Encoding rules: ITU-T X.691 §14
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>objectClass ENUMERATED { reserved(0), logical-device(1) } (Table 23)</li>
 *   <li>ACSIClass ENUMERATED (Table 9)</li>
 *   <li>AddCause CODEDENUM as fixed-length bit string (Table 15)</li>
 * </ul>
 *
 * <p>Encoding:
 * <ul>
 *   <li><b>Non-extensible</b>: ordinal values 0, 1, 2, ... encoded as constrained INTEGER (0..maxOrdinal)</li>
 *   <li><b>Extensible (...)</b>: preamble bit P (0=root, 1=extension), root same as non-extensible,
 *       extension encoded as normally small non-negative integer</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Non-extensible: objectClass { reserved(0), logical-device(1) } ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerEnumerated.encode(pos, 1, 1);       // ordinal=1 (logical-device), maxOrdinal=1
 *   byte[] data = pos.toByteArray();
 *
 *   PerInputStream pis = new PerInputStream(data);
 *   int ordinal = PerEnumerated.decode(pis, 1);  // 1
 *
 *   // --- Extensible enumeration ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   PerEnumerated.encodeExtensible(pos2, false, 1, 2);  // root part, ordinal=1, rootMax=2
 *   byte[] data2 = pos2.toByteArray();
 *
 *   PerInputStream pis2 = new PerInputStream(data2);
 *   EnumeratedResult r = PerEnumerated.decodeExtensible(pis2, 2);
 *   // r.isExtension=false, r.ordinal=1
 * }</pre>
 */
public final class PerEnumerated {

    private PerEnumerated() { /* utility class */ }

    /**
     * Encodes a non-extensible enumerated value.
     *
     * @param pos        output stream
     * @param ordinal    enum ordinal (0-based)
     * @param maxOrdinal maximum ordinal (= number of root values - 1)
     */
    public static void encode(PerOutputStream pos, int ordinal, int maxOrdinal) {
        if (ordinal < 0 || ordinal > maxOrdinal) {
            throw new IllegalArgumentException(
                String.format("Enum ordinal %d out of range [0, %d]", ordinal, maxOrdinal));
        }
        PerInteger.encode(pos, ordinal, 0, maxOrdinal);
    }

    /**
     * Decodes a non-extensible enumerated value.
     *
     * @param pis        input stream
     * @param maxOrdinal maximum ordinal
     * @return enum ordinal
     * @throws PerDecodeException if insufficient data
     */
    public static int decode(PerInputStream pis, int maxOrdinal) throws PerDecodeException {
        return (int) PerInteger.decode(pis, 0, maxOrdinal);
    }

    /**
     * Encodes an extensible enumerated value.
     *
     * @param pos            output stream
     * @param isExtension    true if using extension addition
     * @param ordinal        enum ordinal
     * @param rootMaxOrdinal root part maximum ordinal
     */
    public static void encodeExtensible(PerOutputStream pos,
                                        boolean isExtension, int ordinal, int rootMaxOrdinal) {
        pos.writeBit(isExtension);
        if (!isExtension) {
            PerInteger.encode(pos, ordinal, 0, rootMaxOrdinal);
        } else {
            PerInteger.encodeSmallNonNegative(pos, ordinal);
        }
    }

    /**
     * Decodes an extensible enumerated value.
     *
     * @param pis            input stream
     * @param rootMaxOrdinal root part maximum ordinal
     * @return result containing extension flag and ordinal
     * @throws PerDecodeException if insufficient data
     */
    public static EnumeratedResult decodeExtensible(PerInputStream pis, int rootMaxOrdinal)
            throws PerDecodeException {

        boolean isExtension = pis.readBit();
        if (isExtension) {
            long extOrdinal = PerInteger.decodeSmallNonNegative(pis);
            return new EnumeratedResult(true, (int) extOrdinal);
        } else {
            int ordinal = (int) PerInteger.decode(pis, 0, rootMaxOrdinal);
            return new EnumeratedResult(false, ordinal);
        }
    }

    /** Decode result for extensible ENUMERATED. */
    public static class EnumeratedResult {
        /** Whether an extension addition was used. */
        public final boolean isExtension;
        /** Enum ordinal value. */
        public final int ordinal;

        public EnumeratedResult(boolean isExtension, int ordinal) {
            this.isExtension = isExtension;
            this.ordinal = ordinal;
        }
    }
}
