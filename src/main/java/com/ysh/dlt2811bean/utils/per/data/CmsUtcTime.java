package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

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
 * // Encode / Decode (instance)
 * t.encode(pos);
 * CmsUtcTime r = new CmsUtcTime().decode(pis);
 *
 * // Encode / Decode (static)
 * CmsUtcTime.write(pos, t);
 * CmsUtcTime r = CmsUtcTime.read(pis);
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
public class CmsUtcTime extends AbstractCmsCompound<CmsUtcTime> {

    private final CmsInt32U secondsSinceEpoch;
    private final CmsInt24U fractionOfSecond;
    private final CmsTimeQuality timeQuality;

    public CmsUtcTime() {
        this.secondsSinceEpoch = new CmsInt32U();
        this.fractionOfSecond = new CmsInt24U();
        this.timeQuality = new CmsTimeQuality();
    }

    /** Convenience constructor — accepts raw int for timeQuality. */
    public CmsUtcTime(long secondsSinceEpoch, int fractionOfSecond, int rawTimeQuality) {
        this(secondsSinceEpoch, fractionOfSecond, new CmsTimeQuality(rawTimeQuality));
    }

    public CmsUtcTime(long secondsSinceEpoch, int fractionOfSecond, CmsTimeQuality timeQuality) {
        this();
        setSecondsSinceEpoch(secondsSinceEpoch);
        setFractionOfSecond(fractionOfSecond);
        setTimeQuality(timeQuality);
    }

    // ==================== Getters / Setters ====================

    public long getSecondsSinceEpoch() {
        return secondsSinceEpoch.get();
    }

    public CmsUtcTime setSecondsSinceEpoch(long secondsSinceEpoch) {
        this.secondsSinceEpoch.set(secondsSinceEpoch);
        return this;
    }

    public int getFractionOfSecond() {
        return fractionOfSecond.get();
    }

    public CmsUtcTime setFractionOfSecond(int fractionOfSecond) {
        this.fractionOfSecond.set(fractionOfSecond);
        return this;
    }

    public CmsTimeQuality getTimeQuality() {
        return timeQuality;
    }

    public CmsUtcTime setTimeQuality(CmsTimeQuality timeQuality) {
        this.timeQuality.set(timeQuality.get());
        return this;
    }

    public CmsUtcTime setTimeQuality(int rawTimeQuality) {
        this.timeQuality.set((long) rawTimeQuality);
        return this;
    }

    // ==================== Encode / Decode ====================

    @Override
    public void encode(PerOutputStream pos) {
        secondsSinceEpoch.encode(pos);
        fractionOfSecond.encode(pos);
        timeQuality.encode(pos);
    }

    @Override
    public CmsUtcTime decode(PerInputStream pis) throws Exception {
        secondsSinceEpoch.decode(pis);
        fractionOfSecond.decode(pis);
        timeQuality.decode(pis);
        return this;
    }

    // ==================== Static helpers ====================

    public static void write(PerOutputStream pos, CmsUtcTime value) {
        if (value == null) {
            new CmsUtcTime().encode(pos);
        } else {
            value.encode(pos);
        }
    }

    public static CmsUtcTime read(PerInputStream pis) throws Exception {
        return new CmsUtcTime().decode(pis);
    }

    @Override
    public String toString() {
        return String.format("UtcTime[s=%d, frac=%d, %s]",
                secondsSinceEpoch.get(), fractionOfSecond.get(), timeQuality);
    }
}