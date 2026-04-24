package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 TimeStamp type (§7.3.4).
 *
 * <p>TimeStamp is semantically identical to {@link CmsUtcTime} — same encoding,
 * same fields, same constraints. This class exists purely as a type alias for
 * API clarity where the standard distinguishes TimeStamp from other uses of UtcTime.
 *
 * <pre>
 * // Chain usage
 * CmsTimeStamp ts = new CmsTimeStamp()
 *     .secondsSinceEpoch(new CmsInt32U(1715000000L))
 *     .fractionOfSecond(new CmsInt24U(1234567))
 *     .timeQuality(new CmsTimeQuality(0x20));
 *
 * // Quick mode (raw quality byte)
 * CmsTimeStamp ts = new CmsTimeStamp(1715000000L, 1234567, 0x20L);
 *
 * // Encode / Decode
 * ts.encode(pos);
 * CmsTimeStamp r = new CmsTimeStamp().decode(pis);
 * </pre>
 *
 * @see CmsUtcTime
 */
public class CmsTimeStamp extends CmsUtcTime {

    public CmsTimeStamp() {
        this(0L, 0, 0L);
    }

    /**
     * @deprecated Use {@link CmsUtcTime} directly instead.
     * CmsTimeStamp is an alias kept for backward compatibility.
     */
    @Deprecated(forRemoval = true)
    public CmsTimeStamp(long secondsSinceEpoch, int fractionOfSecond, long timeQualityValue) {
        super(secondsSinceEpoch, fractionOfSecond, timeQualityValue);
    }
}
