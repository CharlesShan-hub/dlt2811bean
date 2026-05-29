package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsBinaryTime extends AbstractCmsCompound {

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

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_BinaryTime(hour, minute, second, millisecond, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBinaryTime decode(byte[] data) {
        IntByReference h = new IntByReference();
        IntByReference min = new IntByReference();
        IntByReference sec = new IntByReference();
        IntByReference ms = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_BinaryTime(data, data.length, h, min, sec, ms);
        return new CmsBinaryTime(h.getValue(), min.getValue(), sec.getValue(), ms.getValue());
    }

    public CmsBinaryTime copy() {
        return new CmsBinaryTime(hour, minute, second, millisecond);
    }
}
