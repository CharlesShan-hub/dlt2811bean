package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import lombok.Getter;

/**
 * DL/T 2811 double-point position type (§7.3.5, Table 8).
 *
 * <pre>
 * ┌──────┬───────┬────────────────────┬──────────────┐
 * │ Bits │ Value │ Meaning            │ Constant     │
 * ├──────┼───────┼────────────────────┼──────────────┤
 * │ 00   │  0    │ intermediate-state │ INTERMEDIATE │
 * │ 01   │  1    │ off                │ OFF          │
 * │ 10   │  2    │ on                 │ ON           │
 * │ 11   │  3    │ bad-state          │ BAD          │
 * └──────┴───────┴────────────────────┴──────────────┘
 * </pre>
 *
 * <p>Encoded as a 2-bit fixed-size bit string.
 *
 * <pre>
 * // Construct
 * CmsDbpos d = new CmsDbpos(CmsDbpos.ON);
 *
 * // Encode / Decode
 * CmsDbpos.encode(pos, d);
 * CmsDbpos r = CmsDbpos.decode(pis);
 *
 * // Construct from raw value
 * CmsDbpos d2 = new CmsDbpos(2);
 *
 * // Check
 * r.getValue(); // → 0..3
 * if (r.getValue() == CmsDbpos.ON) { ... }
 * </pre>
 */
public final class CmsDbpos {

    /** 00 — intermediate-state */
    public static final int INTERMEDIATE = 0;
    /** 01 — off */
    public static final int OFF = 1;
    /** 10 — on */
    public static final int ON = 2;
    /** 11 — bad-state */
    public static final int BAD = 3;

    @Getter
    /** 2-bit position value (0..3). */
    private int value;

    public CmsDbpos() {
        this.value = INTERMEDIATE;
    }

    public CmsDbpos(int value) {
        if (value < 0 || value > 3) {
            throw new IllegalArgumentException("dbpos value out of range (0..3)");
        }
        this.value = value;
    }

    // ==================== Semantic setters ====================

    public CmsDbpos setIntermediate() { this.value = INTERMEDIATE; return this; }
    public CmsDbpos setOff()         { this.value = OFF;         return this; }
    public CmsDbpos setOn()          { this.value = ON;          return this; }
    public CmsDbpos setBad()         { this.value = BAD;         return this; }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsDbpos value) {
        PerBitString.encodeFixedSize(pos, value.value, 2);
    }

    public static CmsDbpos decode(PerInputStream pis) throws PerDecodeException {
        int raw = (int) PerBitString.decodeFixedSize(pis, 2);
        return new CmsDbpos(raw);
    }

    @Override
    public String toString() {
        return "Dbpos[" + value + "]";
    }
}
