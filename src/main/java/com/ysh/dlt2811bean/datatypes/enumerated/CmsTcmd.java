package com.ysh.dlt2811bean.datatypes.enumerated;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 tap command type (§7.3.7, Table 10).
 *
 * <pre>
 * ┌──────┬──────┬──────────────────┬───────────┐
 * │ Bits │ Value │ Meaning          │ Constant  │
 * ├──────┼──────┼──────────────────┼───────────┤
 * │ 00   │ 0    │ stop             │ STOP      │
 * │ 01   │ 1    │ lower            │ LOWER     │
 * │ 10   │ 2    │ higher           │ HIGHER    │
 * │ 11   │ 3    │ reserved         │ RESERVED  │
 * └──────┴──────┴──────────────────┴───────────┘
 * </pre>
 *
 * <p>Encoded as a 2-bit constrained enumeration (ENUMERATED 0..3).
 *
 * <pre>
 * // Construction
 * CmsTcmd cmd = new CmsTcmd();
 * CmsTcmd cmd = new CmsTcmd(CmsTcmd.HIGHER);
 * CmsTcmd cmd = new CmsTcmd(2); // same as HIGHER
 *
 * // Setting values
 * cmd.set(CmsTcmd.LOWER);
 * cmd.set(1); // same as LOWER
 *
 * // Checking values
 * if (cmd.is(CmsTcmd.HIGHER)) { ... }
 * if (cmd.is(2)) { ... } // same as HIGHER
 *
 * // Getting the value
 * int value = cmd.get(); // returns 0, 1, 2, or 3
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * cmd.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsTcmd decoded = new CmsTcmd().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with fixed enumeration values.
 * The size is fixed to 4 (values 0..3).
 */
public class CmsTcmd extends AbstractCmsEnumerated<CmsTcmd> {

    /** 00 — stop */
    public static final int STOP = 0;
    /** 01 — lower */
    public static final int LOWER = 1;
    /** 10 — higher */
    public static final int HIGHER = 2;
    /** 11 — reserved */
    public static final int RESERVED = 3;

    /**
     * Constructs a CmsTcmd with default value STOP (0).
     */
    public CmsTcmd() {
        this(STOP);
    }

    public CmsTcmd(int value) {
        super("CmsTcmd", value, 4);
    }

    private static final CmsTcmd SHARED = new CmsTcmd();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsTcmd read(PerInputStream pis) throws Exception {
        return new CmsTcmd().decode(pis);
    }
}