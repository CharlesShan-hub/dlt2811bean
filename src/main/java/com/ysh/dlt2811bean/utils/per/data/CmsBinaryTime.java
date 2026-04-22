package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;

/**
 * DL/T 2811 BinaryTime type (§7.2.2 / §7.3.9 EntryTime).
 *
 * <pre>
 * ┌──────────────────┬───────────┬────────┬───────────┐
 * │ Field            │ 2811 Type │ Bits   │ Java type │
 * ├──────────────────┼───────────┼────────┼───────────┤
 * │ msOfDay          │ INT32U    │ 32     │ int       │
 * │ daysSince1984    │ INT16U    │ 16     │ int       │
 * └──────────────────┴───────────┴────────┴───────────┘
 * </pre>
 *
 * <p>Total: 48 bits (6 bytes), encoded as a fixed-size octet string.
 * msOfDay: milliseconds elapsed since the most recent midnight.
 * daysSince1984: days elapsed since 1984-01-01 GMT.
 *
 * <pre>
 * // Construct
 * CmsBinaryTime t = new CmsBinaryTime(43200000, 15000);
 *
 * // Encode / Decode
 * CmsBinaryTime.encode(pos, t);
 * CmsBinaryTime r = CmsBinaryTime.decode(pis);
 *
 * // Access fields
 * r.getMsOfDay();       // → 43200000
 * r.getDaysSince1984(); // → 15000
 * </pre>
 */
public class CmsBinaryTime {

    @Getter
    /* Milliseconds since last midnight (INT32U, 0..86399999). */
    private final int msOfDay;
    @Getter
    /* Days since 1984-01-01 GMT (INT16U, 0..65535). */
    private final int daysSince1984;

    public CmsBinaryTime(int msOfDay, int daysSince1984) {
        if (msOfDay < 0 || msOfDay > 86399999) {
            throw new IllegalArgumentException("msOfDay out of range (0..86399999)");
        }
        if (daysSince1984 < 0 || daysSince1984 > 65535) {
            throw new IllegalArgumentException("daysSince1984 out of INT16U range");
        }
        this.msOfDay = msOfDay;
        this.daysSince1984 = daysSince1984;
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes a BinaryTime into the output stream (6 bytes).
     * msOfDay as INT32U (4 bytes) + daysSince1984 as INT16U (2 bytes).
     */
    public static void encode(PerOutputStream pos, CmsBinaryTime value) {
        CmsInt32U.encode(pos, value.msOfDay & 0xFFFFFFFFL);
        CmsInt16U.encode(pos, value.daysSince1984);
    }

    /**
     * Decodes a BinaryTime from the input stream.
     */
    public static CmsBinaryTime decode(PerInputStream pis) throws PerDecodeException {
        int msOfDay       = (int) CmsInt32U.decode(pis).getValue();
        int daysSince1984 = CmsInt16U.decode(pis).getValue();
        return new CmsBinaryTime(msOfDay, daysSince1984);
    }

    @Override
    public String toString() {
        return String.format("BinaryTime[ms=%d, day=%d]", msOfDay, daysSince1984);
    }
}
