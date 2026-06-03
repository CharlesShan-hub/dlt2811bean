package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
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

    public static class NativeStruct extends Structure {
        public int seconds_since_epoch;
        public int fraction_of_second;
        public byte time_quality;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
        }
    }

    public long secondsSinceEpoch;
    public long fractional;

    public CmsTimeStamp() {
        super("TimeStamp");
        nativeStruct = new NativeStruct();
    }

    public CmsTimeStamp(long secondsSinceEpoch, long fractional) {
        this();
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractional = fractional;
    }

    @Override
    protected void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.seconds_since_epoch = (int) secondsSinceEpoch;
        ns.fraction_of_second = (int) fractional;
        ns.time_quality = 0;
    }

    @Override
    protected void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        secondsSinceEpoch = ns.seconds_since_epoch & 0xFFFFFFFFL;
        fractional = ns.fraction_of_second & 0xFFFFFFFFL;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_time_stamp_encode((NativeStruct) nativeStruct, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_time_stamp_decode(data, data.length, nativeStruct);
        ((NativeStruct) nativeStruct).read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 16;
    }

    public static CmsTimeStamp from(byte[] data) {
        return new CmsTimeStamp().decode(data);
    }
}
