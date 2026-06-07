package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import java.time.ZonedDateTime;

public class CmsTimeStamp extends CmsUtcTime {

    @Override
    public CmsTimeStamp set(long epochMs) {
        return (CmsTimeStamp) super.set(epochMs);
    }

    @Override
    public CmsTimeStamp set(int year, int month, int day,
                            int hour, int minute, int second) {
        return (CmsTimeStamp) super.set(year, month, day, hour, minute, second);
    }

    @Override
    public CmsTimeStamp set(int year, int month, int day,
                            int hour, int minute, int second, int millis) {
        return (CmsTimeStamp) super.set(year, month, day, hour, minute, second, millis);
    }

    @Override
    public CmsTimeStamp set(ZonedDateTime dt) {
        return (CmsTimeStamp) super.set(dt);
    }

    @Override
    public CmsTimeStamp now() {
        return (CmsTimeStamp) super.now();
    }

    public static class ByValue extends CmsTimeStamp implements Structure.ByValue {}
}