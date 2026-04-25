package com.ysh.dlt2811bean.data.compound;

import com.ysh.dlt2811bean.data.code.CmsTimeQuality;
import com.ysh.dlt2811bean.data.numeric.CmsInt24U;
import com.ysh.dlt2811bean.data.numeric.CmsInt32U;
import com.ysh.dlt2811bean.data.type.AbstractCmsCompound;
import lombok.Setter;
import lombok.experimental.Accessors;

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
 * // Chain usage
 * CmsUtcTime t = new CmsUtcTime()
 *     .secondsSinceEpoch(new CmsInt32U(1715000000L))
 *     .fractionOfSecond(new CmsInt24U(1234567))
 *     .timeQuality(new CmsTimeQuality(0x20));
 *
 * // Quick mode
 * CmsUtcTime t = new CmsUtcTime(1715000000L, 1234567, new CmsTimeQuality(0x20));
 *
 * // Encode / Decode
 * t.encode(pos);
 * CmsUtcTime r = new CmsUtcTime().decode(pis);
 *
 * // Access fields
 * r.secondsSinceEpoch.get();                     // → 1715000000
 * r.fractionOfSecond.get();                      // → 1234567
 * r.timeQuality.is(CmsTimeQuality.CLOCK_FAULT);  // → boolean
 * r.timeQuality.getSubSecondPrecision();         // → int
 * </pre>
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5905">RFC 5905</a>
 */
@Setter
@Accessors(fluent = true)
public class CmsUtcTime extends AbstractCmsCompound<CmsUtcTime> {

    public CmsInt32U secondsSinceEpoch = new CmsInt32U(0L);
    public CmsInt24U fractionOfSecond = new CmsInt24U(0);
    public CmsTimeQuality timeQuality = new CmsTimeQuality();

    public CmsUtcTime() {
        super("UtcTime");
        registerField("secondsSinceEpoch");
        registerField("fractionOfSecond");
        registerField("timeQuality");
    }

    public CmsUtcTime(long secondsSinceEpoch, int fractionOfSecond, long timeQualityValue) {
        this();
        this.secondsSinceEpoch.set(secondsSinceEpoch);
        this.fractionOfSecond.set(fractionOfSecond);
        this.timeQuality.set(timeQualityValue);
    }
}