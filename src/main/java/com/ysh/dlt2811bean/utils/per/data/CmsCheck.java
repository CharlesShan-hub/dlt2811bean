package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;

/**
 * DL/T 2811 control operation check type (§7.5.3, Table 14).
 *
 * <pre>
 * ┌──────┬───────┬──────────────────────────────┬──────────────────────┐
 * │ Bits │ Value │ Meaning                      │ Constant             │
 * ├──────┼───────┼──────────────────────────────┼──────────────────────┤
 * │ x0   │  0    │ no synchrocheck              │ SYNCHROCHECK_NONE    │
 * │ x1   │  1    │ synchrocheck required        │ SYNCHROCHECK_YES     │
 * │ 0x   │  0    │ no interlock-check           │ INTERLOCK_NONE       │
 * │ 1x   │  1    │ interlock-check required     │ INTERLOCK_YES        │
 * └──────┴───────┴──────────────────────────────┴──────────────────────┘
 * </pre>
 *
 * <p>Bit 0 = synchrocheck, Bit 1 = interlock-check.
 * Encoded as a 2-bit CODED ENUM (fixed-size bit string).
 *
 * <pre>
 * // Construct
 * CmsCheck c = new CmsCheck();
 * c.bits().setBit(0, true);  // synchrocheck
 * c.bits().setBit(1, true);  // interlock
 *
 * // Or from raw value
 * CmsCheck c2 = new CmsCheck(0b11);
 *
 * // Encode / Decode
 * CmsCheck.encode(pos, c);
 * CmsCheck r = CmsCheck.decode(pis);
 *
 * // Query
 * r.is(0);  // synchrocheck?
 * r.is(1);  // interlock?
 * </pre>
 */
public final class CmsCheck {

    public static final int BIT_SYNCHROCHECK = 0;
    public static final int BIT_INTERLOCK = 1;
    public static final int SIZE = 2;

    @Getter
    private final CmsCodedEnum bits;

    public CmsCheck() {
        this.bits = new CmsCodedEnum(0, SIZE);
    }

    public CmsCheck(int raw) {
        if (raw < 0 || raw > 3) {
            throw new IllegalArgumentException("check value out of range (0..3)");
        }
        this.bits = new CmsCodedEnum(raw, SIZE);
    }

    /**
     * Check if the given bit is set.
     * Use with constants: {@code check.is(CmsCheck.BIT_SYNCHROCHECK)}
     */
    public boolean is(int bit) {
        if (bit < 0 || bit >= SIZE) throw new IllegalArgumentException("bit out of range (0..1): " + bit);
        return bits.testBit(bit);
    }

    /** Convert to raw 2-bit value. */
    public int toRaw() {
        return (int) bits.getValue();
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsCheck value) {
        CmsCodedEnum.encode(pos, value.bits);
    }

    public static void encode(PerOutputStream pos, int raw) {
        CmsCodedEnum.encode(pos, raw, SIZE);
    }

    public static CmsCheck decode(PerInputStream pis) throws PerDecodeException {
        return new CmsCheck((int) CmsCodedEnum.decode(pis, SIZE).getValue());
    }

    @Override
    public String toString() {
        return String.format("Check[synchrocheck=%s,interlock=%s]",
                is(BIT_SYNCHROCHECK), is(BIT_INTERLOCK));
    }
}
