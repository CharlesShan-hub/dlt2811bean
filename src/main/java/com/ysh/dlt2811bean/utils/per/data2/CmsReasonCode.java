package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 reason code for reports (§7.6.3).
 *
 * <p>CODED ENUM — fixed 7-bit string, each bit is a named flag.
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
 * │  6  │ application-trigger     │
 * └─────┴─────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Bean mode — chain setters with bit constants
 * import static com.ysh.dlt2811bean.utils.per.data.CmsReasonCode.*;
 *
 * CmsReasonCode rc = new CmsReasonCode()
 *     .set(DATA_CHANGE, true)
 *     .set(INTEGRITY, true);
 *
 * // Check
 * rc.is(DATA_CHANGE);     // → true
 * rc.is(APPLICATION_TRIGGER);  // → false
 *
 * // Construct from raw value
 * CmsReasonCode rc2 = new CmsReasonCode(0x12); // bits 1,4 set
 *
 * // Encode / Decode
 * CmsReasonCode.encode(pos, rc);
 * CmsReasonCode r = CmsReasonCode.decode(pis);
 * </pre>
 */
public final class CmsReasonCode {

    public static final int SIZE = 7;

    // ==================== Bit position constants ====================

    /** Bit 0 — reserved (should always be 0). */
    public static final int RESERVED = 0;
    /** Bit 1 — report triggered by data value change. */
    public static final int DATA_CHANGE = 1;
    /** Bit 2 — report triggered by quality change. */
    public static final int QUALITY_CHANGE = 2;
    /** Bit 3 — report triggered by data update. */
    public static final int DATA_UPDATE = 3;
    /** Bit 4 — integrity period report. */
    public static final int INTEGRITY = 4;
    /** Bit 5 — report triggered by general interrogation. */
    public static final int GENERAL_INTERROGATION = 5;
    /** Bit 6 — report triggered by application. */
    public static final int APPLICATION_TRIGGER = 6;

    // ==================== Internal storage ====================

    private final CmsCodedEnum bits;

    // ==================== Constructors ====================

    public CmsReasonCode() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    /**
     * Constructs from raw 7-bit value.
     * Only lower 7 bits are used; higher bits are ignored.
     */
    public CmsReasonCode(int raw) {
        this.bits = new CmsCodedEnum(raw & 0x7F, SIZE);
    }

    private CmsReasonCode(CmsCodedEnum bits) {
        this.bits = bits;
    }

    // ==================== Generic bit access ====================

    /**
     * Tests whether the bit at the given position is set.
     *
     * @param bit bit position (0-based, must be 0..6)
     * @return true if the bit is set
     * @throws IllegalArgumentException if bit is out of range
     */
    public boolean is(int bit) {
        checkRange(bit);
        return bits.testBit(bit);
    }

    /**
     * Sets or clears the bit at the given position.
     *
     * @param bit bit position (0-based, must be 0..6)
     * @param value true to set, false to clear
     * @return this for chaining
     * @throws IllegalArgumentException if bit is out of range
     */
    public CmsReasonCode set(int bit, boolean value) {
        checkRange(bit);
        bits.setBit(bit, value);
        return this;
    }

    // ==================== Convenience helpers ====================

    /** Returns true if any reason is set (excluding reserved). */
    public boolean hasAnyReason() {
        return bits.getValue() >> 1 != 0;
    }

    /** Returns the raw 7-bit value. */
    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes ReasonCode as a fixed 7-bit BIT STRING.
     */
    public static void encode(PerOutputStream pos, CmsReasonCode value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    /**
     * Decodes ReasonCode from a fixed 7-bit BIT STRING.
     */
    public static CmsReasonCode decode(PerInputStream pis) throws PerDecodeException {
        return new CmsReasonCode(CmsCodedEnum.decode(pis, SIZE));
    }

    // ==================== Internal ====================

    private static void checkRange(int bit) {
        if (bit < 0 || bit >= SIZE) {
            throw new IllegalArgumentException(
                    "bit must be 0.." + (SIZE - 1) + ", got: " + bit);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ReasonCode[");
        boolean first = true;
        if (is(RESERVED))              { sb.append("reserved"); first = false; }
        if (is(DATA_CHANGE))           { if (!first) sb.append(","); sb.append("data-change"); first = false; }
        if (is(QUALITY_CHANGE))        { if (!first) sb.append(","); sb.append("quality-change"); first = false; }
        if (is(DATA_UPDATE))           { if (!first) sb.append(","); sb.append("data-update"); first = false; }
        if (is(INTEGRITY))             { if (!first) sb.append(","); sb.append("integrity"); first = false; }
        if (is(GENERAL_INTERROGATION)) { if (!first) sb.append(","); sb.append("general-interrogation"); first = false; }
        if (is(APPLICATION_TRIGGER))   { if (!first) sb.append(","); sb.append("application-trigger"); }
        if (first) sb.append("none");
        sb.append("]");
        return sb.toString();
    }
}
