package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * ASN.1 NULL type — APER codec.
 *
 * <p>ASN.1 type: <b>NULL</b> (ITU-T X.680 §18)
 * <br>Encoding rules: ITU-T X.691 §13
 *
 * <p>DL/T 2811 usage: empty response bodies (e.g. DeleteFile Response+, Associate Response+).
 *
 * <p>Encoding: 0 bits. No data is written or read — the type itself carries all information.
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- Encode ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerNull.encode(pos);                   // writes nothing
 *   byte[] data = pos.toByteArray();       // [] (empty)
 *
 *   // --- Decode ---
 *   PerInputStream pis = new PerInputStream(data);
 *   PerNull.decode(pis);                   // reads nothing
 * }</pre>
 */
public final class PerNull {

    private PerNull() { /* utility class */ }

    /**
     * Encodes NULL (writes nothing).
     *
     * @param pos output stream
     */
    public static void encode(PerOutputStream pos) {
        // No data to encode
    }

    /**
     * Decodes NULL (reads nothing).
     *
     * @param pis input stream
     */
    public static void decode(PerInputStream pis) {
        // No data to read
    }
}
