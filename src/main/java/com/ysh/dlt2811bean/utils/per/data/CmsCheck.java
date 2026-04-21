package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
 * Encoded as a 2-bit fixed-size bit string.
 *
 * <pre>
 * // Construct with raw value
 * CmsCheck c = new CmsCheck(0b11); // both checks enabled
 *
 * // Construct with semantic setters
 * CmsCheck c2 = new CmsCheck()
 *     .setSynchrocheck(true)
 *     .setInterlock(true);
 *
 * // Encode / Decode
 * CmsCheck.encode(pos, c);
 * CmsCheck r = CmsCheck.decode(pis);
 *
 * // Query individual flags
 * r.isSynchrocheck(); // → boolean
 * r.isInterlock();    // → boolean
 * </pre>
 */
public final class CmsCheck {

    @Getter
    @Setter
    @Accessors(chain = true)
    /** Whether synchrocheck is required (bit 0). */
    private boolean synchrocheck;

    @Getter
    @Setter
    @Accessors(chain = true)
    /** Whether interlock-check is required (bit 1). */
    private boolean interlock;

    public CmsCheck() {
        this.synchrocheck = false;
        this.interlock = false;
    }

    /**
     * Construct from raw 2-bit value.
     *
     * @param raw 2-bit value (0..3)
     */
    public CmsCheck(int raw) {
        if (raw < 0 || raw > 3) {
            throw new IllegalArgumentException("check value out of range (0..3)");
        }
        this.synchrocheck = (raw & 0b01) != 0;
        this.interlock = (raw & 0b10) != 0;
    }

    /**
     * Convert to raw 2-bit value.
     */
    public int toRaw() {
        int raw = 0;
        if (synchrocheck) raw |= 0b01;
        if (interlock)    raw |= 0b10;
        return raw;
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsCheck value) {
        PerBitString.encodeFixedSize(pos, value.toRaw(), 2);
    }

    public static CmsCheck decode(PerInputStream pis) throws PerDecodeException {
        int raw = (int) PerBitString.decodeFixedSize(pis, 2);
        return new CmsCheck(raw);
    }

    @Override
    public String toString() {
        return "Check[interlock=" + interlock + ",synchrocheck=" + synchrocheck + "]";
    }
}
