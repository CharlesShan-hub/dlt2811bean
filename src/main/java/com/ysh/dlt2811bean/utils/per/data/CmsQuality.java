package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 quality type (§7.3.6, Table 9).
 *
 * <p>Encoded as a 13-bit fixed-size bit string (CODED ENUM).
 *
 * <pre>
 * ┌──────┬──────┬────────────────────┐
 * │ Bits │ Type │ Meaning            │
 * ├──────┼──────┼────────────────────┤
 * │  0~1 │ 2bit │ validity           │
 * │    2 │ flag │ overflow           │
 * │    3 │ flag │ outOfRange         │
 * │    4 │ flag │ badReference       │
 * │    5 │ flag │ oscillatory        │
 * │    6 │ flag │ failure            │
 * │    7 │ flag │ oldData            │
 * │    8 │ flag │ inconsistent       │
 * │    9 │ flag │ inaccurate         │
 * │   10 │ flag │ source             │
 * │   11 │ flag │ test               │
 * │   12 │ flag │ operatorBlocked    │
 * └──────┴──────┴────────────────────┘
 * </pre>
 *
 * <pre>
 * // Bean mode — flags via is/set, multi-bit fields via getValidity/setValidity
 * CmsQuality q = new CmsQuality()
 *     .setValidity(CmsQuality.INVALID)
 *     .set(CmsQuality.OVERFLOW, true)
 *     .set(CmsQuality.TEST, true)
 *     .set(CmsQuality.SOURCE, true);
 *
 * // Check
 * q.is(CmsQuality.OVERFLOW);
 * q.getValidity();  // → 0..3
 *
 * // Encode / Decode
 * CmsQuality.encode(pos, q);
 * CmsQuality r = CmsQuality.decode(pis);
 * </pre>
 */
public final class CmsQuality {

    public static final int SIZE = 13;

    // ==================== Validity values (2-bit, bits 0~1) ====================
    public static final int GOOD = 0;
    public static final int INVALID = 1;
    public static final int RESERVED = 2;
    public static final int QUESTIONABLE = 3;

    // ==================== Bit positions ====================
    /** Bits 0~1 — validity (2-bit field). */
    public static final int VALIDITY = 0;
    public static final int VALIDITY_WIDTH = 2;
    /** Bit 2 — measurement overflow. */
    public static final int OVERFLOW = 2;
    /** Bit 3 — value out of range. */
    public static final int OUT_OF_RANGE = 3;
    /** Bit 4 — bad reference. */
    public static final int BAD_REFERENCE = 4;
    /** Bit 5 — oscillatory. */
    public static final int OSCILLATORY = 5;
    /** Bit 6 — failure. */
    public static final int FAILURE = 6;
    /** Bit 7 — old data. */
    public static final int OLD_DATA = 7;
    /** Bit 8 — inconsistent. */
    public static final int INCONSISTENT = 8;
    /** Bit 9 — inaccurate. */
    public static final int INACCURATE = 9;
    /** Bit 10 — source (0=process, 1=substituted). */
    public static final int SOURCE = 10;
    /** Bit 11 — test mode. */
    public static final int TEST = 11;
    /** Bit 12 — operator blocked. */
    public static final int OPERATOR_BLOCKED = 12;

    private final CmsCodedEnum bits;

    public CmsQuality() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    public CmsQuality(int raw) {
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    // ==================== Single-bit flag access ====================

    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        return bits.testBit(bit);
    }

    public CmsQuality set(int bit, boolean value) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        bits.setBit(bit, value);
        return this;
    }

    // ==================== Multi-bit field access ====================

    /** Gets the validity field (bits 0~1, 2-bit). */
    public int getValidity() {
        return bits.getBits(VALIDITY, VALIDITY_WIDTH);
    }

    /** Sets the validity field (bits 0~1, 2-bit). */
    public CmsQuality setValidity(int value) {
        bits.setBits(VALIDITY, VALIDITY_WIDTH, value);
        return this;
    }

    // ==================== Raw conversion ====================

    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsQuality value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    public static CmsQuality decode(PerInputStream pis) throws PerDecodeException {
        return new CmsQuality((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        return String.format("Quality[0x%04X, v=%d]", toRaw(), getValidity());
    }
}
