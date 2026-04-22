package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 time quality type (Table 7, §7.3.4 TimeStamp).
 *
 * <p>Encoded as an 8-bit fixed-size bit string (CODED ENUM).
 *
 * <pre>
 * ┌──────┬──────┬────────────────────────────────────┐
 * │ Bits │ Type │ Meaning                            │
 * ├──────┼──────┼────────────────────────────────────┤
 * │    0 │ flag │ leapSecondKnown                   │
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
 * // Bean mode — flags via is/set, precision via getter/setter
 * CmsTimeQuality tq = new CmsTimeQuality()
 *     .set(CmsTimeQuality.LEAP_SECOND_KNOWN, true)
 *     .set(CmsTimeQuality.CLOCK_FAULT, true)
 *     .setSubSecondPrecision(24);
 *
 * // Check
 * tq.is(CmsTimeQuality.CLOCK_FAULT);
 * tq.getSubSecondPrecision();  // → 24
 *
 * // Encode / Decode
 * CmsTimeQuality.encode(pos, tq);
 * CmsTimeQuality r = CmsTimeQuality.decode(pis);
 * </pre>
 */
public final class CmsTimeQuality {

    public static final int SIZE = 8;

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

    private final CmsCodedEnum bits;

    public CmsTimeQuality() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    public CmsTimeQuality(int raw) {
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    // ==================== Single-bit flag access ====================

    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        return bits.testBit(bit);
    }

    public CmsTimeQuality set(int bit, boolean value) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        bits.setBit(bit, value);
        return this;
    }

    // ==================== Multi-bit field access ====================

    /** Gets the sub-second precision (bits 3~7, 5-bit). */
    public int getSubSecondPrecision() {
        return bits.getBits(SUB_SECOND_PRECISION, SUB_SECOND_PRECISION_WIDTH);
    }

    /** Sets the sub-second precision (bits 3~7, 5-bit). */
    public CmsTimeQuality setSubSecondPrecision(int value) {
        bits.setBits(SUB_SECOND_PRECISION, SUB_SECOND_PRECISION_WIDTH, value);
        return this;
    }

    // ==================== Raw conversion ====================

    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsTimeQuality value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    public static CmsTimeQuality decode(PerInputStream pis) throws PerDecodeException {
        return new CmsTimeQuality((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        return String.format("TimeQuality[0x%02X, precision=%d]", toRaw(), getSubSecondPrecision());
    }
}
