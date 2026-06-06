package com.ysh.jcms.datatype.extended;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsInt16U;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.ffi.CmsType;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsBinaryTime extends CmsType {
    public static class ByValue extends CmsBinaryTime implements Structure.ByValue {}

    /** Days from 1984-01-01 to Java epoch (1970-01-01). */
    private static final long DAYS_EPOCH_OFFSET = 5113;
    private static final long MS_PER_DAY = 86400000L;

    public CmsInt32U.ByValue msOfDay = new CmsInt32U.ByValue();
    public CmsInt16U.ByValue daysSince1984 = new CmsInt16U.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("msOfDay", "daysSince1984");
    }

    /** Set from Java epoch milliseconds (UTC). */
    public CmsBinaryTime set(long epochMs) {
        long totalDays = epochMs / MS_PER_DAY;
        daysSince1984.value((short) (totalDays - DAYS_EPOCH_OFFSET));
        msOfDay.value((int) (epochMs % MS_PER_DAY));
        return this;
    }

    /** Set from date/time components (UTC). */
    public CmsBinaryTime set(int year, int month, int day,
                             int hour, int minute, int second) {
        return set(year, month, day, hour, minute, second, 0);
    }

    /** Set from date/time components with milliseconds (UTC). */
    public CmsBinaryTime set(int year, int month, int day,
                             int hour, int minute, int second, int millis) {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(year, month - 1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, millis);
        return set(cal.getTimeInMillis());
    }

    /** Set from a Java 8 {@code ZonedDateTime}. */
    public CmsBinaryTime set(ZonedDateTime dt) {
        return set(dt.toInstant().toEpochMilli());
    }

    /** Set to current system time (UTC). */
    public CmsBinaryTime now() {
        return set(System.currentTimeMillis());
    }
}