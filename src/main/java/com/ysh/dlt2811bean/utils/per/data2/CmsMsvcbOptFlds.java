package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

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
 * // Bean mode — set via int constants
 * CmsMsvcbOptFlds opt = new CmsMsvcbOptFlds()
 *     .set(CmsMsvcbOptFlds.REFRESH_TIME, true)
 *     .set(CmsMsvcbOptFlds.SAMPLE_RATE, true);
 *
 * // Check
 * opt.is(CmsMsvcbOptFlds.REFRESH_TIME);
 *
 * // Encode / Decode
 * CmsMsvcbOptFlds.encode(pos, opt);
 * CmsMsvcbOptFlds r = CmsMsvcbOptFlds.decode(pis);
 * </pre>
 */
public final class CmsMsvcbOptFlds {

    public static final int SIZE = 5;

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

    private final CmsCodedEnum bits;

    public CmsMsvcbOptFlds() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    public CmsMsvcbOptFlds(int raw) {
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        return bits.testBit(bit);
    }

    public CmsMsvcbOptFlds set(int bit, boolean value) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        bits.setBit(bit, value);
        return this;
    }

    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsMsvcbOptFlds value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    public static CmsMsvcbOptFlds decode(PerInputStream pis) throws PerDecodeException {
        return new CmsMsvcbOptFlds((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MsvcbOptFlds[");
        boolean first = true;
        if (is(REFRESH_TIME))   { sb.append("refresh-time"); first = false; }
        if (is(RESERVED))       { if (!first) sb.append(","); sb.append("reserved"); first = false; }
        if (is(SAMPLE_RATE))    { if (!first) sb.append(","); sb.append("sample-rate"); first = false; }
        if (is(DATA_SET_NAME))  { if (!first) sb.append(","); sb.append("data-set-name"); first = false; }
        if (is(SECURITY))       { if (!first) sb.append(","); sb.append("security"); }
        if (first) sb.append("none");
        sb.append("]");
        return sb.toString();
    }
}
