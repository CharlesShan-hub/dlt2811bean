package com.ysh.dlt2811bean.data.code;

/**
 * DL/T 2811 time quality type (Table 7, §7.3.4 TimeStamp).
 *
 * <p>Encoded as an 8-bit fixed-size BIT STRING (CODED ENUM).
 *
 * <pre>
 * ┌──────┬──────┬────────────────────────────────────┐
 * │ Bits │ Type │ Meaning                            │
 * ├──────┼──────┼────────────────────────────────────┤
 * │    0 │ flag │ leapSecondKnown                    │
 * │    1 │ flag │ clockFault                         │
 * │    2 │ flag │ clockNotSynced                     │
 * │  3~7 │ 5bit │ subSecondPrecision                 │
 * └──────┴──────┴────────────────────────────────────┘
 * </pre>
 *
 * <p>Sub-second precision values (5-bit):
 * <ul>
 *   <li>0~24 — number of significant bits of fractionOfSecond</li>
 *   <li>25~30 — illegal</li>
 *   <li>31 — not specified</li>
 * </ul>
 *
 * <pre>
 * // Construction
 * CmsTimeQuality tq = new CmsTimeQuality();
 * CmsTimeQuality tq = new CmsTimeQuality(0x07); // from raw 8-bit value
 *
 * // Setting and checking flags
 * tq.setBit(CmsTimeQuality.CLOCK_FAULT, true);
 * tq.setBit(CmsTimeQuality.LEAP_SECOND_KNOWN, true);
 * tq.testBit(CmsTimeQuality.CLOCK_FAULT); // true
 *
 * // Multi-bit field access
 * tq.setBits(CmsTimeQuality.SUB_SECOND_PRECISION, CmsTimeQuality.SUB_SECOND_PRECISION_WIDTH, 24);
 * tq.testBits(CmsTimeQuality.SUB_SECOND_PRECISION, CmsTimeQuality.SUB_SECOND_PRECISION_WIDTH, 24); // true
 *
 * // Getting the raw value
 * long raw = tq.get();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * tq.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsTimeQuality decoded = new CmsTimeQuality().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 8-bit CODED ENUM layout.
 * Bits 0~2 are flags; bits 3~7 form a 5-bit sub-second precision field.
 */
import com.ysh.dlt2811bean.data.type.AbstractCmsCodedEnum;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

public class CmsTimeQuality extends AbstractCmsCodedEnum<CmsTimeQuality> {

    // ==================== Single-bit flags ====================

    /** Bit 0 — leap second is known. */
    public static final int LEAP_SECOND_KNOWN = 0;
    /** Bit 1 — clock failure. */
    public static final int CLOCK_FAULT = 1;
    /** Bit 2 — clock is not synchronized. */
    public static final int CLOCK_NOT_SYNCED = 2;

    // ==================== Multi-bit field ====================

    /** Bits 3~7 — sub-second precision (5-bit). */
    public static final int SUB_SECOND_PRECISION = 3;
    public static final int SUB_SECOND_PRECISION_WIDTH = 5;

    /**
     * Constructs a CmsTimeQuality with default value 0 (all flags cleared).
     */
    public CmsTimeQuality() {
        this(0L);
    }

    /**
     * Constructs a CmsTimeQuality from a raw 8-bit value.
     *
     * @param value raw 8-bit CODED ENUM value
     */
    public CmsTimeQuality(long value) {
        super("CmsTimeQuality", value, 8);
    }

    private static final CmsTimeQuality SHARED = new CmsTimeQuality();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsTimeQuality read(PerInputStream pis) throws Exception {
        return new CmsTimeQuality().decode(pis);
    }

    // ==================== Convenience methods ====================

    /**
     * Gets the sub-second precision (bits 3~7, 5-bit).
     *
     * @return precision value 0~31:
     *         <ul>
     *           <li>0~24 — number of significant bits of fractionOfSecond</li>
     *           <li>25~30 — illegal</li>
     *           <li>31 — not specified</li>
     *         </ul>
     */
    public int getSubSecondPrecision() {
        return (int) getBits(SUB_SECOND_PRECISION, SUB_SECOND_PRECISION_WIDTH);
    }

    /**
     * Sets the sub-second precision (bits 3~7, 5-bit).
     *
     * @param precision precision value 0~31
     * @return this instance for chaining
     */
    public CmsTimeQuality setSubSecondPrecision(int precision) {
        setBits(SUB_SECOND_PRECISION, SUB_SECOND_PRECISION_WIDTH, precision);
        return this;
    }
}