package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 RCB (Report Control Block) optional fields (§7.6.4, Table 16).
 *
 * <p>Encoded as a fixed-size BIT STRING of 10 bits (CODED ENUM).
 *
 * <pre>
 * ┌─────┬─────────────────────────────┐
 * │ Bit │ Meaning                     │
 * ├─────┼─────────────────────────────┤
 * │  0  │ reserved                    │
 * │  1  │ sequence-number             │
 * │  2  │ report-time-stamp           │
 * │  3  │ reason-for-inclusion        │
 * │  4  │ data-set-name               │
 * │  5  │ data-reference              │
 * │  6  │ buffer-overflow             │
 * │  7  │ entryID                     │
 * │  8  │ conf-revision               │
 * │  9  │ segmentation                │
 * └─────┴─────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Bean mode — set via int constants
 * CmsRcbOptFlds opt = new CmsRcbOptFlds()
 *     .set(CmsRcbOptFlds.SEQUENCE_NUMBER, true)
 *     .set(CmsRcbOptFlds.REASON_FOR_INCLUSION, true);
 *
 * // Check
 * opt.is(CmsRcbOptFlds.SEQUENCE_NUMBER);
 *
 * // Encode / Decode
 * CmsRcbOptFlds.encode(pos, opt);
 * CmsRcbOptFlds r = CmsRcbOptFlds.decode(pis);
 * </pre>
 */
public final class CmsRcbOptFlds {

    public static final int SIZE = 10;

    /** Bit 0 — reserved. */
    public static final int RESERVED = 0;
    /** Bit 1 — include sequence number in report. */
    public static final int SEQUENCE_NUMBER = 1;
    /** Bit 2 — include report time stamp. */
    public static final int REPORT_TIME_STAMP = 2;
    /** Bit 3 — include reason for inclusion. */
    public static final int REASON_FOR_INCLUSION = 3;
    /** Bit 4 — include data set name. */
    public static final int DATA_SET_NAME = 4;
    /** Bit 5 — include data reference. */
    public static final int DATA_REFERENCE = 5;
    /** Bit 6 — buffer overflow indicator (invalid for URCB, must be 0). */
    public static final int BUFFER_OVERFLOW = 6;
    /** Bit 7 — include entry ID. */
    public static final int ENTRY_ID = 7;
    /** Bit 8 — include configuration revision. */
    public static final int CONF_REVISION = 8;
    /** Bit 9 — segmentation indicator. */
    public static final int SEGMENTATION = 9;

    private final CmsCodedEnum bits;

    public CmsRcbOptFlds() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    public CmsRcbOptFlds(int raw) {
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        return bits.testBit(bit);
    }

    public CmsRcbOptFlds set(int bit, boolean value) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        bits.setBit(bit, value);
        return this;
    }

    /** Clears buffer-overflow bit for URCB usage. */
    public CmsRcbOptFlds clearBufferOverflowForUrcb() {
        bits.setBit(BUFFER_OVERFLOW, false);
        return this;
    }

    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsRcbOptFlds value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    public static CmsRcbOptFlds decode(PerInputStream pis) throws PerDecodeException {
        return new CmsRcbOptFlds((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RcbOptFlds[");
        boolean first = true;
        if (is(RESERVED))              { sb.append("reserved"); first = false; }
        if (is(SEQUENCE_NUMBER))       { if (!first) sb.append(","); sb.append("sequence-number"); first = false; }
        if (is(REPORT_TIME_STAMP))     { if (!first) sb.append(","); sb.append("report-time-stamp"); first = false; }
        if (is(REASON_FOR_INCLUSION))  { if (!first) sb.append(","); sb.append("reason-for-inclusion"); first = false; }
        if (is(DATA_SET_NAME))         { if (!first) sb.append(","); sb.append("data-set-name"); first = false; }
        if (is(DATA_REFERENCE))        { if (!first) sb.append(","); sb.append("data-reference"); first = false; }
        if (is(BUFFER_OVERFLOW))       { if (!first) sb.append(","); sb.append("buffer-overflow"); first = false; }
        if (is(ENTRY_ID))              { if (!first) sb.append(","); sb.append("entryID"); first = false; }
        if (is(CONF_REVISION))         { if (!first) sb.append(","); sb.append("conf-revision"); first = false; }
        if (is(SEGMENTATION))          { if (!first) sb.append(","); sb.append("segmentation"); }
        if (first) sb.append("none");
        sb.append("]");
        return sb.toString();
    }
}
