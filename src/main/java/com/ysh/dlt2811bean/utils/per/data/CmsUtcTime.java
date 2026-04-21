package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBitString;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;

/**
 * DL/T 2811 UtcTime type (§7.2.1 / §7.3.4 TimeStamp, RFC 5905 encoding).
 *
 * <pre>
 * ┌───────────────────────┬───────────┬────────┬───────────┐
 * │ Field                 │ 2811 Type │ Bits   │ Java type │
 * ├───────────────────────┼───────────┼────────┼───────────┤
 * │ secondsSinceEpoch     │ INT32U    │ 32     │ long      │
 * │ fractionOfSecond      │ INT24U    │ 24     │ int       │
 * │ timeQuality           │ BIT STR   │ 8      │ int       │
 * └───────────────────────┴───────────┴────────┴───────────┘
 * </pre>
 *
 * <p>Total: 64 bits (8 bytes), aligned. Encoding order: seconds (MSB first),
 * then fraction (MSB first), then timeQuality (MSB first).
 *
 * <pre>
 * // Construct
 * CmsUtcTime t = new CmsUtcTime(1715000000L, 1234567, 0x20);
 *
 * // Encode / Decode
 * CmsUtcTime.encode(pos, t);
 * CmsUtcTime r = CmsUtcTime.decode(pis);
 *
 * // Access fields
 * r.getSecondsSinceEpoch(); // → 1715000000
 * r.getFractionOfSecond();  // → 1234567
 * r.getTimeQuality();       // → 0x20
 * </pre>
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5905">RFC 5905</a>
 */
public final class CmsUtcTime {

    @Getter
    /* Seconds since 1970-01-01 00:00:00 UTC (INT32U). */
    private final long secondsSinceEpoch;
    @Getter
    /* Fraction of current second, in units of 1/2^24 s (INT24U). */
    private final int fractionOfSecond;
    @Getter
    /* Time quality bit flags (8-bit, see Table 7). */
    private final int timeQuality;

    public CmsUtcTime(long secondsSinceEpoch, int fractionOfSecond, int timeQuality) {
        if (secondsSinceEpoch < 0 || secondsSinceEpoch > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("secondsSinceEpoch out of INT32U range");
        }
        if (fractionOfSecond < 0 || fractionOfSecond > 0xFFFFFF) {
            throw new IllegalArgumentException("fractionOfSecond out of INT24U range");
        }
        if (timeQuality < 0 || timeQuality > 0xFF) {
            throw new IllegalArgumentException("timeQuality out of 8-bit range");
        }
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractionOfSecond = fractionOfSecond;
        this.timeQuality = timeQuality;
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes a UtcTime into the output stream (8 bytes, aligned).
     */
    public static void encode(PerOutputStream pos, CmsUtcTime value) {
        PerInteger.encode(pos, value.secondsSinceEpoch & 0xFFFFFFFFL, 0, 4294967295L);
        encodeUint24(pos, value.fractionOfSecond);
        PerBitString.encodeFixedSize(pos, value.timeQuality, 8);
    }

    /**
     * Decodes a UtcTime from the input stream.
     */
    public static CmsUtcTime decode(PerInputStream pis) throws PerDecodeException {
        long seconds = PerInteger.decode(pis, 0, 4294967295L);
        int fraction = decodeUint24(pis);
        int quality = (int) PerBitString.decodeFixedSize(pis, 8);
        return new CmsUtcTime(seconds, fraction, quality);
    }

    // ==================== INT24U (private, only used here) ====================

    private static void encodeUint24(PerOutputStream pos, int value) {
        PerInteger.encode(pos, value & 0xFFFFFFL, 0, 16777215);
    }

    private static int decodeUint24(PerInputStream pis) throws PerDecodeException {
        return (int) PerInteger.decode(pis, 0, 16777215);
    }

    @Override
    public String toString() {
        return String.format("UtcTime[s=%d, frac=%d, q=0x%02X]",
                secondsSinceEpoch, fractionOfSecond, timeQuality);
    }
}
