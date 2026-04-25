package com.ysh.dlt2811bean.data.code;

/**
 * DL/T 2811 MSVCB (Multicast Sampled Value Control Block) optional fields (§7.6.6, Table 17).
 *
 * <p>Encoded as a fixed-size BIT STRING of 5 bits (CODED ENUM).
 *
 * <pre>
 * ┌─────┬─────────────────────────────┐
 * │ Bit │ Meaning                     │
 * ├─────┼─────────────────────────────┤
 * │  0  │ refresh-time                │
 * │  1  │ reserved                    │
 * │  2  │ sample-rate                 │
 * │  3  │ data-set-name               │
 * │  4  │ security                    │
 * └─────┴─────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * CmsMsvcbOptFlds opt = new CmsMsvcbOptFlds();
 * CmsMsvcbOptFlds opt = new CmsMsvcbOptFlds(0x05); // from raw 5-bit value (bits 0,2)
 *
 * // Setting and checking flags
 * opt.setBit(CmsMsvcbOptFlds.REFRESH_TIME, true);
 * opt.setBit(CmsMsvcbOptFlds.SAMPLE_RATE, true);
 * opt.testBit(CmsMsvcbOptFlds.REFRESH_TIME); // true
 *
 * // Getting the raw value
 * long raw = opt.get();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * opt.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsMsvcbOptFlds decoded = new CmsMsvcbOptFlds().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 5-bit CODED ENUM layout.
 * Bits 0~4 are MSVCB optional field flags.
 */
import com.ysh.dlt2811bean.data.type.AbstractCmsCodedEnum;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

public class CmsMsvcbOptFlds extends AbstractCmsCodedEnum<CmsMsvcbOptFlds> {

    // ==================== Bit position constants ====================

    /** Bit 0 — include refresh time. */
    public static final int REFRESH_TIME = 0;
    /** Bit 1 — reserved. */
    public static final int RESERVED = 1;
    /** Bit 2 — include sample rate. */
    public static final int SAMPLE_RATE = 2;
    /** Bit 3 — include data set name. */
    public static final int DATA_SET_NAME = 3;
    /** Bit 4 — include security information. */
    public static final int SECURITY = 4;

    /**
     * Constructs a CmsMsvcbOptFlds with default value 0 (all flags cleared).
     */
    public CmsMsvcbOptFlds() {
        this(0L);
    }

    /**
     * Constructs a CmsMsvcbOptFlds from a raw 5-bit value.
     *
     * @param value raw 5-bit CODED ENUM value
     */
    public CmsMsvcbOptFlds(long value) {
        super("CmsMsvcbOptFlds", value, 5);
    }

    private static final CmsMsvcbOptFlds SHARED = new CmsMsvcbOptFlds();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsMsvcbOptFlds read(PerInputStream pis) throws Exception {
        return new CmsMsvcbOptFlds().decode(pis);
    }
}