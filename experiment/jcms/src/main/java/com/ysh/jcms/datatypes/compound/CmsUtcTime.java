package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.CmsUtcTimeStruct;

public class CmsUtcTime {

    private int secondsSinceEpoch;
    private int fractionOfSecond;
    private CmsTimeQuality timeQuality;

    public CmsUtcTime() {
        timeQuality = new CmsTimeQuality();
    }

    public CmsUtcTime(int secondsSinceEpoch, int fractionOfSecond, CmsTimeQuality timeQuality) {
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractionOfSecond = fractionOfSecond;
        this.timeQuality = timeQuality;
    }

    public int getSecondsSinceEpoch() { return secondsSinceEpoch; }
    public void setSecondsSinceEpoch(int secondsSinceEpoch) { this.secondsSinceEpoch = secondsSinceEpoch; }
    public int getFractionOfSecond() { return fractionOfSecond; }
    public void setFractionOfSecond(int fractionOfSecond) { this.fractionOfSecond = fractionOfSecond; }
    public CmsTimeQuality getTimeQuality() { return timeQuality; }
    public void setTimeQuality(CmsTimeQuality timeQuality) { this.timeQuality = timeQuality; }

    public long toMillis() {
        return (long)secondsSinceEpoch * 1000
             + (((long)fractionOfSecond * 1000) / 16777216);
    }

    public static CmsUtcTime fromMillis(long ms) {
        CmsUtcTime utc = new CmsUtcTime();
        utc.secondsSinceEpoch = (int)(ms / 1000);
        utc.fractionOfSecond = (int)(((ms % 1000) * 16777216) / 1000);
        return utc;
    }

    public byte[] encode() {
        CmsUtcTimeStruct.ByReference s = new CmsUtcTimeStruct.ByReference();
        s.seconds_since_epoch = secondsSinceEpoch;
        s.fraction_of_second = fractionOfSecond;
        s.time_quality = (byte)((timeQuality.tagf & 0x07) | ((timeQuality.precision & 0x1F) << 3));
        s.write();
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_utc_time_encode(s, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsUtcTime decode(byte[] data) {
        CmsUtcTimeStruct.ByReference s = new CmsUtcTimeStruct.ByReference();
        CmsFFIDatatypes.INSTANCE.cms_utc_time_decode(data, data.length, s);
        s.read();
        CmsUtcTime utc = new CmsUtcTime();
        utc.secondsSinceEpoch = s.seconds_since_epoch;
        utc.fractionOfSecond = s.fraction_of_second;
        utc.timeQuality.tagf = s.time_quality & 0x07;
        utc.timeQuality.precision = (s.time_quality >> 3) & 0x1F;
        return utc;
    }

    public CmsUtcTime copy() {
        return new CmsUtcTime(secondsSinceEpoch, fractionOfSecond,
            new CmsTimeQuality(timeQuality.tagf, timeQuality.precision, timeQuality.fraction));
    }
}
