package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerEnumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 sampling mode for MSVCB (§7.6.7, Table 18).
 *
 * <p>Encoded as ENUMERATED (§7.1.6), range 0..2.
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
 * <p>Used in Multicast Sampled Value Control Blocks to indicate how
 * the sampling rate (smpRate) should be interpreted.
 *
 * <pre>
 * // Create
 * CmsSmpMod mode = new CmsSmpMod(CmsSmpMod.SAMPLES_PER_SECOND);
 *
 * // Encode / Decode
 * CmsSmpMod.encode(pos, mode);
 * CmsSmpMod r = CmsSmpMod.decode(pis);
 *
 * // Check
 * String name = mode.getCodeName(); // "samples-per-second"
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsSmpMod {

    public static final int SAMPLES_PER_NOMINAL_PERIOD = 0;
    public static final int SAMPLES_PER_SECOND = 1;
    public static final int SECONDS_PER_SAMPLE = 2;

    /** Maximum valid code (2). */
    public static final int MAX_CODE = 2;

    private int code;

    public CmsSmpMod() {
        this.code = SAMPLES_PER_NOMINAL_PERIOD;
    }

    public CmsSmpMod(int code) {
        if (code < 0 || code > MAX_CODE) {
            throw new IllegalArgumentException(
                String.format("SmpMod code %d out of range [0, %d]", code, MAX_CODE));
        }
        this.code = code;
    }

    /** Returns the symbolic name for known codes, or "unknown-" + code. */
    public String getCodeName() {
        switch (code) {
            case SAMPLES_PER_NOMINAL_PERIOD: return "samples-per-nominal-period";
            case SAMPLES_PER_SECOND: return "samples-per-second";
            case SECONDS_PER_SAMPLE: return "seconds-per-sample";
            default: return "unknown-" + code;
        }
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes SmpMod as ENUMERATED (0..2).
     * <p>Range 0..2 requires ceil(log2(3)) = 2 bits in constrained PER.
     */
    public static void encode(PerOutputStream pos, CmsSmpMod value) {
        PerEnumerated.encode(pos, value.code, MAX_CODE);
    }

    /**
     * Decodes SmpMod from ENUMERATED (0..2).
     */
    public static CmsSmpMod decode(PerInputStream pis) throws PerDecodeException {
        return new CmsSmpMod(PerEnumerated.decode(pis, MAX_CODE));
    }

    @Override
    public String toString() {
        return String.format("SmpMod[%d=%s]", code, getCodeName());
    }
}
