package com.ysh.dlt2811bean.datatypes.enumerated;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 sampling mode for MSVCB (§7.6.7, Table 18).
 *
 * <pre>
 * ┌─────┬─────────────────────────────┐
 * │ Code│ Meaning                     │
 * ├─────┼─────────────────────────────┤
 * │  0  │ samples-per-nominal-period  │
 * │  1  │ samples-per-second          │
 * │  2  │ seconds-per-sample          │
 * └─────┴─────────────────────────────┘
 * </pre>
 *
 * <p>Encoded as a constrained ENUMERATED (0..2), 2 bits in PER.
 *
 * <p>Used in Multicast Sampled Value Control Blocks to indicate how
 * the sampling rate (smpRate) should be interpreted.
 *
 * <pre>
 * // Construction
 * CmsSmpMod mode = new CmsSmpMod();
 * CmsSmpMod mode = new CmsSmpMod(CmsSmpMod.SAMPLES_PER_SECOND);
 *
 * // Setting and checking
 * mode.set(CmsSmpMod.SECONDS_PER_SAMPLE);
 * if (mode.is(CmsSmpMod.SAMPLES_PER_SECOND)) { ... }
 *
 * // Encode / Decode
 * mode.encode(pos);
 * CmsSmpMod r = new CmsSmpMod().decode(pis);
 * </pre>
 */
public class CmsSmpMod extends AbstractCmsEnumerated<CmsSmpMod> {

    /** 0 — samples-per-nominal-period */
    public static final int SAMPLES_PER_NOMINAL_PERIOD = 0;
    /** 1 — samples-per-second */
    public static final int SAMPLES_PER_SECOND = 1;
    /** 2 — seconds-per-sample */
    public static final int SECONDS_PER_SAMPLE = 2;

    /**
     * Constructs a CmsSmpMod with default value SAMPLES_PER_NOMINAL_PERIOD (0).
     */
    public CmsSmpMod() {
        this(SAMPLES_PER_NOMINAL_PERIOD);
    }

    public CmsSmpMod(int value) {
        super("CmsSmpMod", value, 3); // size = 3: values 0..2
    }

    private static final CmsSmpMod SHARED = new CmsSmpMod();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsSmpMod read(PerInputStream pis) throws Exception {
        return new CmsSmpMod().decode(pis);
    }
}
