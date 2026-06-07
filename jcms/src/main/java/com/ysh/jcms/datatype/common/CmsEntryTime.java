package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import java.time.ZonedDateTime;

public class CmsEntryTime extends CmsBinaryTime {

    @Override
    public CmsEntryTime set(long epochMs) {
        return (CmsEntryTime) super.set(epochMs);
    }

    @Override
    public CmsEntryTime set(int year, int month, int day,
                            int hour, int minute, int second) {
        return (CmsEntryTime) super.set(year, month, day, hour, minute, second);
    }

    @Override
    public CmsEntryTime set(int year, int month, int day,
                            int hour, int minute, int second, int millis) {
        return (CmsEntryTime) super.set(year, month, day, hour, minute, second, millis);
    }

    @Override
    public CmsEntryTime set(ZonedDateTime dt) {
        return (CmsEntryTime) super.set(dt);
    }

    @Override
    public CmsEntryTime now() {
        return (CmsEntryTime) super.now();
    }

    public static class ByValue extends CmsEntryTime implements Structure.ByValue {}
}