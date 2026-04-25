package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASN.1 CHOICE type — APER codec.
 *
 * <p>ASN.1 type: <b>CHOICE</b> (ITU-T X.680 §15)
 * <br>Encoding rules: ITU-T X.691 §17
 *
 * <p>In APER, a non-extensible CHOICE is encoded as:
 * <ol>
 *   <li>A <b>normally small non-negative integer</b> encoding the selected index (0-based)</li>
 *   <li>The encoded value of the chosen alternative</li>
 * </ol>
 *
 * <p>This class only handles the index encoding/decoding. The caller is responsible
 * for encoding/decoding the actual value based on the decoded index.
 *
 * <p>For extensible CHOICE (with {@code ...}), use {@link #encodeExtensible} /
 * {@link #decodeExtensible}.
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>Data CHOICE with 24 alternatives (§7.7.1)</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Non-extensible CHOICE ---
 *   // Encode: choose alternative at index 5
 *   PerOutputStream pos = new PerOutputStream();
 *   PerChoice.encode(pos, 5);       // index 5
 *   // ... then encode the actual value of alternative 5
 *
 *   // Decode: read which alternative was chosen
 *   PerInputStream pis = new PerInputStream(data);
 *   int index = PerChoice.decode(pis);   // e.g. 5
 *   // ... then decode the actual value based on index
 *
 *   // --- Extensible CHOICE ---
 *   PerChoice.encodeExtensible(pos, true, 2);   // extension, index 2
 *   ChoiceResult r = PerChoice.decodeExtensible(pis);
 *   // r.isExtension=true, r.index=2
 * }</pre>
 */
public final class PerChoice {

    private PerChoice() { /* utility class */ }

    // ==================== Non-extensible ====================

    /**
     * Encodes the index of the chosen alternative for a non-extensible CHOICE.
     *
     * <p>The index is encoded as a <b>normally small non-negative integer</b>,
     * which uses 1 byte for values 0..127 (high bit = 0, lower 7 bits = value).
     *
     * @param pos   output stream
     * @param index 0-based index of the chosen alternative
     */
    public static void encode(PerOutputStream pos, int index) {
        if (index < 0) {
            throw new IllegalArgumentException(
                String.format("CHOICE index must be >= 0, got %d", index));
        }
        PerInteger.encodeSmallNonNegative(pos, index);
    }

    /**
     * Decodes the index of the chosen alternative for a non-extensible CHOICE.
     *
     * @param pis input stream
     * @return 0-based index of the chosen alternative
     * @throws PerDecodeException if insufficient data
     */
    public static int decode(PerInputStream pis) throws PerDecodeException {
        return (int) PerInteger.decodeSmallNonNegative(pis);
    }

    // ==================== Extensible ====================

    /**
     * Encodes the choice index for an extensible CHOICE.
     *
     * @param pos         output stream
     * @param isExtension true if using an extension addition
     * @param index       alternative index (root or extension)
     */
    public static void encodeExtensible(PerOutputStream pos,
                                        boolean isExtension, int index) {
        pos.writeBit(isExtension);
        if (isExtension) {
            PerInteger.encodeSmallNonNegative(pos, index);
        } else {
            // Root alternatives: normally small non-negative integer
            PerInteger.encodeSmallNonNegative(pos, index);
        }
    }

    /**
     * Decodes the choice index for an extensible CHOICE.
     *
     * @param pis input stream
     * @return result containing extension flag and index
     * @throws PerDecodeException if insufficient data
     */
    public static ChoiceResult decodeExtensible(PerInputStream pis) throws PerDecodeException {
        boolean isExtension = pis.readBit();
        long index = PerInteger.decodeSmallNonNegative(pis);
        return new ChoiceResult(isExtension, (int) index);
    }

    /** Decode result for extensible CHOICE. */
    public static class ChoiceResult {
        /** Whether an extension addition was chosen. */
        public final boolean isExtension;
        /** 0-based index of the chosen alternative. */
        public final int index;

        public ChoiceResult(boolean isExtension, int index) {
            this.isExtension = isExtension;
            this.index = index;
        }
    }
}
