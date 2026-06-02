package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

public class CmsUtcTime extends AbstractCmsCompound<CmsUtcTime> {

    public static class NativeStruct extends Structure {
        public int seconds_since_epoch;
        public int fraction_of_second;
        public byte time_quality;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
        }
    }

    public int seconds_since_epoch;
    public int fraction_of_second;
    public byte time_quality;

    private final NativeStruct nativeStruct;

    public CmsUtcTime() {
        super("UtcTime");
        this.nativeStruct = new NativeStruct();
        setNativeStruct(nativeStruct);
    }

    private void syncToNative() {
        nativeStruct.seconds_since_epoch = seconds_since_epoch;
        nativeStruct.fraction_of_second = fraction_of_second;
        nativeStruct.time_quality = time_quality;
    }

    private void syncFromNative() {
        seconds_since_epoch = nativeStruct.seconds_since_epoch;
        fraction_of_second = nativeStruct.fraction_of_second;
        time_quality = nativeStruct.time_quality;
    }

    public CmsUtcTime(int secondsSinceEpoch, int fractionOfSecond, int tagf, int precision) {
        this();
        this.seconds_since_epoch = secondsSinceEpoch;
        this.fraction_of_second = fractionOfSecond;
        this.time_quality = (byte)((tagf & 0x07) | ((precision & 0x1F) << 3));
    }

    public int getTagf() {
        return time_quality & 0x07;
    }

    public int getPrecision() {
        return (time_quality >> 3) & 0x1F;
    }

    public long toMillis() {
        return (long)seconds_since_epoch * 1000
             + (((long)fraction_of_second * 1000) / 16777216);
    }

    public static CmsUtcTime fromMillis(long ms) {
        CmsUtcTime utc = new CmsUtcTime();
        utc.seconds_since_epoch = (int)(ms / 1000);
        utc.fraction_of_second = (int)(((ms % 1000) * 16777216) / 1000);
        return utc;
    }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_utc_time_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsUtcTime decode(byte[] data) {
        CmsUtcTime utc = new CmsUtcTime();
        CmsFFIDatatypes.INSTANCE.cms_utc_time_decode(data, data.length, utc.nativeStruct);
        utc.nativeStruct.read();
        utc.syncFromNative();
        return utc;
    }

    public CmsUtcTime copy() {
        CmsUtcTime clone = new CmsUtcTime();
        clone.seconds_since_epoch = this.seconds_since_epoch;
        clone.fraction_of_second = this.fraction_of_second;
        clone.time_quality = this.time_quality;
        return clone;
    }
}
