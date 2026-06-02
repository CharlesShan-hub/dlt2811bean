package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsBinaryTime extends AbstractCmsCompound<CmsBinaryTime> {

    private final int hour;
    private final int minute;
    private final int second;
    private final int millisecond;

    public CmsBinaryTime(int hour, int minute, int second, int millisecond) {
        super("BinaryTime");
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getSecond() { return second; }
    public int getMillisecond() { return millisecond; }

    private int toMsOfDay() {
        return ((hour * 60 + minute) * 60 + second) * 1000 + millisecond;
    }

    public byte[] encode() {
        int msOfDay = toMsOfDay();
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_binary_time_encode(msOfDay, 0, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBinaryTime decode(byte[] data) {
        IntByReference msOfDay = new IntByReference();
        IntByReference daysSince1984 = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_binary_time_decode(data, data.length, msOfDay, daysSince1984);
        int totalMs = msOfDay.getValue();
        int h = totalMs / 3600000;
        int rem = totalMs % 3600000;
        int min = rem / 60000;
        rem %= 60000;
        int sec = rem / 1000;
        int ms = rem % 1000;
        return new CmsBinaryTime(h, min, sec, ms);
    }

    public CmsBinaryTime copy() {
        return new CmsBinaryTime(hour, minute, second, millisecond);
    }
}
