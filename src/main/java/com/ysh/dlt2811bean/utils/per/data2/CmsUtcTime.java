package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;

/**
 * DL/T 2811 UtcTime type (§7.2.1 / §7.3.4 TimeStamp, RFC 5905 encoding).
 *
 * <pre>
 * ┌───────────────────────┬───────────┬────────┬──────────────────┐
 * │ Field                 │ 2811 Type │ Bits   │ Java type        │
 * ├───────────────────────┼───────────┼────────┼──────────────────┤
 * │ secondsSinceEpoch     │ INT32U    │ 32     │ long             │
 * │ fractionOfSecond      │ INT24U    │ 24     │ int              │
 * │ timeQuality           │ CODEDENUM │ 8      │ CmsTimeQuality   │
 * └───────────────────────┴───────────┴────────┴──────────────────┘
 * </pre>
 *
 * <p>Total: 64 bits (8 bytes), aligned. Encoding order: seconds (MSB first),
 * then fraction (MSB first), then timeQuality (MSB first).
 *
 * <pre>
 * // Bean mode — chain setters
 * CmsUtcTime t = new CmsUtcTime()
 *     .setSecondsSinceEpoch(1715000000L)
 *     .setFractionOfSecond(1234567)
 *     .setTimeQuality(new CmsTimeQuality().setSubSecondPrecision(24));
 *
 * // Quick mode — raw values
 * CmsUtcTime t = new CmsUtcTime(1715000000L, 1234567, new CmsTimeQuality(0x20));
 *
 * // Encode / Decode
 * CmsUtcTime.encode(pos, t);
 * CmsUtcTime r = CmsUtcTime.decode(pis);
 *
 * // Access fields
 * r.getSecondsSinceEpoch();                     // → 1715000000
 * r.getFractionOfSecond();                      // → 1234567
 * r.getTimeQuality().is(CmsTimeQuality.CLOCK_FAULT);  // → boolean
 * r.getTimeQuality().getSubSecondPrecision();   // → int
 * </pre>
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5905">RFC 5905</a>
 */
@Getter
public class CmsUtcTime {

    /* Seconds since 1970-01-01 00:00:00 UTC (INT32U). */
    private long secondsSinceEpoch;

    /* Fraction of current second, in units of 1/2^24 s (INT24U). */
    private int fractionOfSecond;

    /* Time quality (CODED ENUM, 8-bit). */
    private CmsTimeQuality timeQuality;

    public CmsUtcTime() {
        this.secondsSinceEpoch = 0;
        this.fractionOfSecond = 0;
        this.timeQuality = new CmsTimeQuality();
    }

    /** Convenience constructor — accepts raw int for timeQuality. */
    public CmsUtcTime(long secondsSinceEpoch, int fractionOfSecond, int rawTimeQuality) {
        this(secondsSinceEpoch, fractionOfSecond, new CmsTimeQuality(rawTimeQuality));
    }

    public CmsUtcTime(long secondsSinceEpoch, int fractionOfSecond, CmsTimeQuality timeQuality) {
        if (secondsSinceEpoch < 0 || secondsSinceEpoch > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("secondsSinceEpoch out of INT32U range");
        }
        if (fractionOfSecond < 0 || fractionOfSecond > 0xFFFFFF) {
            throw new IllegalArgumentException("fractionOfSecond out of INT24U range");
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
        CmsInt32U.encode(pos, value.secondsSinceEpoch & 0xFFFFFFFFL);
        encodeUint24(pos, value.fractionOfSecond);
        CmsTimeQuality.encode(pos, value.timeQuality);
    }

    /**
     * Decodes a UtcTime from the input stream.
     */
    public static CmsUtcTime decode(PerInputStream pis) throws PerDecodeException {
        long seconds = CmsInt32U.decode(pis).getValue();
        int fraction = decodeUint24(pis);
        CmsTimeQuality quality = CmsTimeQuality.decode(pis);
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
        return String.format("UtcTime[s=%d, frac=%d, %s]",
                secondsSinceEpoch, fractionOfSecond, timeQuality);
    }
}
