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
public class CmsTimeStamp extends AbstractCmsCompound<CmsTimeStamp> {

    public int seconds_since_epoch;   /* INT32U — use getSecondsSinceEpoch() for unsigned long */
    public int fraction_of_second;
    public byte time_quality;

    public CmsTimeStamp() {
        super("TimeStamp");
    }

    public CmsTimeStamp(long secondsSinceEpoch, long fractional) {
        this();
        this.seconds_since_epoch = (int) secondsSinceEpoch;
        this.fraction_of_second = (int) fractional;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
    }

    /** Unsigned seconds since epoch. */
    public long getSecondsSinceEpoch() {
        return seconds_since_epoch & 0xFFFFFFFFL;
    }

    /** Unsigned fraction of second. */
    public long getFractionOfSecond() {
        return fraction_of_second & 0xFFFFFFFFL;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_time_stamp_encode(this, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_time_stamp_decode(data, data.length, this);
    }

    @Override
    protected int encodeBufSize() {
        return 16;
    }

    public static CmsTimeStamp from(byte[] data) {
        return new CmsTimeStamp().decode(data);
    }
}
