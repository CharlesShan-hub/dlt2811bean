package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 LCB (Log Control Block) optional fields (§7.6.5).
 *
 * <p>Encoded as a fixed-size BIT STRING of 1 bit (CODED ENUM).
 *
 * <pre>
 * ┌─────┬─────────────────────────────┐
 * │ Bit │ Meaning                     │
 * ├─────┼─────────────────────────────┤
 * │  0  │ always 1 (reserved)         │
 * └─────┴─────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Encode / Decode
 * CmsLcbOptFlds.encode(pos);
 * CmsLcbOptFlds.decode(pis);
 * </pre>
 */
public final class CmsLcbOptFlds {

    public static final int SIZE = 1;

    public static final int RESERVED = 0;

    private final CmsCodedEnum bits;

    public CmsLcbOptFlds() {
        this.bits = new CmsCodedEnum(1, SIZE);
    }

    private CmsLcbOptFlds(int raw) {
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range: " + bit);
        return bits.testBit(bit);
    }

    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsLcbOptFlds value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    /** Decodes LCB OptFlds. Always returns a CmsLcbOptFlds with bit=1. */
    public static CmsLcbOptFlds decode(PerInputStream pis) throws PerDecodeException {
        return new CmsLcbOptFlds((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        return "LcbOptFlds[1]";
    }
}
