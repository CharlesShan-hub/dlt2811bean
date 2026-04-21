package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import lombok.Getter;

/**
 * DL/T 2811 tap command type (§7.3.7, Table 10).
 *
 * <pre>
 * ┌──────┬──────┬──────────────────┬───────────┐
 * │ Bits │ Value │ Meaning          │ Constant  │
 * ├──────┼──────┼──────────────────┼───────────┤
 * │ 00   │ 0    │ stop             │ STOP      │
 * │ 01   │ 1    │ lower            │ LOWER     │
 * │ 10   │ 2    │ higher           │ HIGHER    │
 * │ 11   │ 3    │ reserved         │ RESERVED  │
 * └──────┴──────┴──────────────────┴───────────┘
 * </pre>
 *
 * <p>Encoded as a 2-bit fixed-size bit string.
 *
 * <pre>
 * // Create
 * CmsTcmd t = new CmsTcmd(CmsTcmd.HIGHER);
 *
 * // Semantic setters
 * t.setStop();
 * t.setLower();
 * t.setHigher();
 *
 * // Encode / Decode
 * CmsTcmd.encode(pos, t);
 * CmsTcmd r = CmsTcmd.decode(pis);
 * r.getValue(); // → STOP/LOWER/HIGHER/RESERVED
 * </pre>
 */
public final class CmsTcmd {

    /** 00 — stop */
    public static final int STOP = 0;
    /** 01 — lower */
    public static final int LOWER = 1;
    /** 10 — higher */
    public static final int HIGHER = 2;
    /** 11 — reserved */
    public static final int RESERVED = 3;

    @Getter
    private int value;

    public CmsTcmd() {
        this.value = STOP;
    }

    public CmsTcmd(int value) {
        if (value < 0 || value > 3) {
            throw new IllegalArgumentException("value out of range (0..3)");
        }
        this.value = value;
    }

    // ==================== Semantic setters ====================

    public CmsTcmd setStop()     { this.value = STOP; return this; }
    public CmsTcmd setLower()    { this.value = LOWER; return this; }
    public CmsTcmd setHigher()   { this.value = HIGHER; return this; }
    public CmsTcmd setReserved() { this.value = RESERVED; return this; }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsTcmd value) {
        PerBitString.encodeFixedSize(pos, value.value, 2);
    }

    public static CmsTcmd decode(PerInputStream pis) throws PerDecodeException {
        int raw = (int) PerBitString.decodeFixedSize(pis, 2);
        return new CmsTcmd(raw);
    }

    @Override
    public String toString() {
        return "Tcmd[" + value + "]";
    }
}
