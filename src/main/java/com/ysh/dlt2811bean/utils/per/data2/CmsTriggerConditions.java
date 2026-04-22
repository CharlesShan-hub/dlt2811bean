package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 trigger conditions for control blocks (§7.6.2).
 *
 * <p>Encoded as a fixed-size BIT STRING of 6 bits (CODED ENUM).
 *
 * <pre>
 * ┌─────┬─────────────────────────┐
 * │ Bit │ Meaning                 │
 * ├─────┼─────────────────────────┤
 * │  0  │ reserved (always 0)     │
 * │  1  │ data-change             │
 * │  2  │ quality-change          │
 * │  3  │ data-update             │
 * │  4  │ integrity               │
 * │  5  │ general-interrogation   │
 * └─────┴─────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Bean mode — set via int constants
 * CmsTriggerConditions tc = new CmsTriggerConditions()
 *     .set(CmsTriggerConditions.DATA_CHANGE, true)
 *     .set(CmsTriggerConditions.INTEGRITY, true);
 *
 * // Check
 * tc.is(CmsTriggerConditions.DATA_CHANGE);
 *
 * // Encode / Decode
 * CmsTriggerConditions.encode(pos, tc);
 * CmsTriggerConditions r = CmsTriggerConditions.decode(pis);
 * </pre>
 */
public final class CmsTriggerConditions {

    public static final int SIZE = 6;

    /** Bit 0 — reserved (should always be 0). */
    public static final int RESERVED = 0;
    /** Bit 1 — trigger on data value change. */
    public static final int DATA_CHANGE = 1;
    /** Bit 2 — trigger on quality change. */
    public static final int QUALITY_CHANGE = 2;
    /** Bit 3 — trigger on data update. */
    public static final int DATA_UPDATE = 3;
    /** Bit 4 — trigger for integrity period. */
    public static final int INTEGRITY = 4;
    /** Bit 5 — trigger on general interrogation. */
    public static final int GENERAL_INTERROGATION = 5;

    private final CmsCodedEnum bits;

    public CmsTriggerConditions() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    public CmsTriggerConditions(int raw) {
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        return bits.testBit(bit);
    }

    public CmsTriggerConditions set(int bit, boolean value) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        bits.setBit(bit, value);
        return this;
    }

    /** Returns true if any trigger condition is set (excluding reserved). */
    public boolean hasAnyCondition() {
        return is(DATA_CHANGE) || is(QUALITY_CHANGE) || is(DATA_UPDATE)
                || is(INTEGRITY) || is(GENERAL_INTERROGATION);
    }

    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsTriggerConditions value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    public static CmsTriggerConditions decode(PerInputStream pis) throws PerDecodeException {
        return new CmsTriggerConditions((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TriggerConditions[");
        boolean first = true;
        if (is(RESERVED))              { sb.append("reserved"); first = false; }
        if (is(DATA_CHANGE))           { if (!first) sb.append(","); sb.append("data-change"); first = false; }
        if (is(QUALITY_CHANGE))        { if (!first) sb.append(","); sb.append("quality-change"); first = false; }
        if (is(DATA_UPDATE))           { if (!first) sb.append(","); sb.append("data-update"); first = false; }
        if (is(INTEGRITY))             { if (!first) sb.append(","); sb.append("integrity"); first = false; }
        if (is(GENERAL_INTERROGATION)) { if (!first) sb.append(","); sb.append("general-interrogation"); }
        if (first) sb.append("none");
        sb.append("]");
        return sb.toString();
    }
}
