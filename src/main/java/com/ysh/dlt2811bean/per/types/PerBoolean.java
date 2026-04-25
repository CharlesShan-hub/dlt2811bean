package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ASN.1 BOOLEAN type — APER codec.
 *
 * <p>ASN.1 type: <b>BOOLEAN</b> (ITU-T X.680 §16)
 * <br>Encoding rules: ITU-T X.691 §12
 *
 * <p>DL/T 2811 usage: moreFollows flag in paginated service responses, boolean fields
 * in various control service parameters.
 *
 * <p>Encoding: exactly 1 bit. FALSE → 0, TRUE → 1. No alignment required.
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Encode ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerBoolean.encode(pos, true);          // writes 1 bit: 1
 *   PerBoolean.encode(pos, false);         // writes 1 bit: 0
 *   byte[] data = pos.toByteArray();       // [0b1_0_000000] = 0x80
 *
 *   // --- Decode ---
 *   PerInputStream pis = new PerInputStream(data);
 *   boolean a = PerBoolean.decode(pis);    // true
 *   boolean b = PerBoolean.decode(pis);    // false
 * }</pre>
 */
public final class PerBoolean {

    private PerBoolean() { /* utility class */ }

    /**
     * Encodes a boolean value as a single bit.
     *
     * @param pos   output stream
     * @param value boolean value
     */
    public static void encode(PerOutputStream pos, boolean value) {
        pos.writeBit(value);
    }

    /**
     * Decodes a single bit as a boolean value.
     *
     * @param pis input stream
     * @return decoded boolean (true=1, false=0)
     * @throws PerDecodeException if insufficient data
     */
    public static boolean decode(PerInputStream pis) throws PerDecodeException {
        return pis.readBit();
    }
}
