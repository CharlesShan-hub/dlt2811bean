package com.ysh.jcms.datatype.extended;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsInt24U;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.ffi.CmsType;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsUtcTime extends CmsType {

    /** Max value for the 24-bit fraction field (2^24 - 1). */
    private static final long FRAC_MAX = (1L << 24) - 1;
    private static final long NS_PER_SEC = 1_000_000_000L;

    public CmsInt32U.ByValue seconds_since_epoch = new CmsInt32U.ByValue();
    public CmsInt24U.ByValue fraction_of_second = new CmsInt24U.ByValue();
    public CmsTimeQuality.ByValue time_quality = new CmsTimeQuality.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
    }

    /** Set from Java epoch milliseconds (UTC). Sub-second part is derived from the millis remainder. */
    public CmsUtcTime set(long epochMs) {
        seconds_since_epoch.value((int) (epochMs / 1000));
        long millis = epochMs % 1000;
        fraction_of_second.value((int) (millis * (FRAC_MAX + 1) / 1000));
        time_quality.leap_seconds_known().value(true);
        time_quality.clock_failure().value(false);
        time_quality.clock_not_synchronized().value(false);
        time_quality.precision().value(31);  // 11111 = not specified
        return this;
    }

    /** Set from epoch nanoseconds (UTC) for sub-millisecond precision. */
    public CmsUtcTime setNanos(long epochNs) {
        seconds_since_epoch.value((int) (epochNs / NS_PER_SEC));
        long ns = epochNs % NS_PER_SEC;
        fraction_of_second.value((int) (ns * (FRAC_MAX + 1) / NS_PER_SEC));
        time_quality.leap_seconds_known().value(true);
        time_quality.clock_failure().value(false);
        time_quality.clock_not_synchronized().value(false);
        time_quality.precision().value(31);
        return this;
    }

    /** Set from date/time components (UTC). */
    public CmsUtcTime set(int year, int month, int day,
                          int hour, int minute, int second) {
        return set(year, month, day, hour, minute, second, 0);
    }

    /** Set from date/time components with milliseconds (UTC). */
    public CmsUtcTime set(int year, int month, int day,
                          int hour, int minute, int second, int millis) {
        java.util.Calendar cal =
            java.util.GregorianCalendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        cal.set(year, month - 1, day, hour, minute, second);
        cal.set(java.util.Calendar.MILLISECOND, millis);
        return set(cal.getTimeInMillis());
    }

    /** Set from a Java 8 {@code ZonedDateTime}. */
    public CmsUtcTime set(ZonedDateTime dt) {
        return set(dt.toInstant().toEpochMilli());
    }

    /** Set to current system time (UTC). */
    public CmsUtcTime now() {
        return set(System.currentTimeMillis());
    }

    public static class ByValue extends CmsUtcTime implements Structure.ByValue {
        @Override
        public ByValue set(long epochMs) { return (ByValue) super.set(epochMs); }
    }
}