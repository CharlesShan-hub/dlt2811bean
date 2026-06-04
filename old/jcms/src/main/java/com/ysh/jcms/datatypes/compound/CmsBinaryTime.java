package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CmsBinaryTime extends AbstractCmsCompound<CmsBinaryTime> {

    public int msOfDay;
    public short daysSince1984;

    public CmsBinaryTime() {
        super("BinaryTime");
    }

    public CmsBinaryTime(int msOfDay, int daysSince1984) {
        this();
        this.msOfDay = msOfDay;
        this.daysSince1984 = (short) daysSince1984;
    }

    public CmsBinaryTime(int hour, int minute, int second, int millisecond, int daysSince1984) {
        this(((hour * 60 + minute) * 60 + second) * 1000 + millisecond, daysSince1984);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("msOfDay", "daysSince1984");
    }

    public int getHour() { return msOfDay / 3600000; }
    public int getMinute() { return (msOfDay % 3600000) / 60000; }
    public int getSecond() { return (msOfDay % 60000) / 1000; }
    public int getMillisecond() { return msOfDay % 1000; }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_binary_time_encode(this, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_binary_time_decode(data, data.length, this);
    }

    @Override
    protected int encodeBufSize() {
        return 16;
    }

    public static CmsBinaryTime from(byte[] data) {
        return new CmsBinaryTime().decode(data);
    }
}
