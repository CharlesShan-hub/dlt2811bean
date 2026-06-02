package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

import java.util.Arrays;
import java.util.List;

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

    private final NativeStruct nativeStruct;
    private final long secondsSinceEpoch;
    private final long fractional;

    public CmsTimeStamp(long secondsSinceEpoch, long fractional) {
        super("TimeStamp");
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractional = fractional;
        this.nativeStruct = new NativeStruct();
        setNativeStruct(nativeStruct);
    }

    public long getSecondsSinceEpoch() { return secondsSinceEpoch; }
    public long getFractional() { return fractional; }

    public byte[] encode() {
        nativeStruct.seconds_since_epoch = (int) secondsSinceEpoch;
        nativeStruct.fraction_of_second = (int) fractional;
        nativeStruct.time_quality = 0;
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_time_stamp_encode(nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTimeStamp decode(byte[] data) {
        NativeStruct ns = new NativeStruct();
        CmsFFIDatatypes.INSTANCE.cms_time_stamp_decode(data, data.length, ns);
        ns.read();
        return new CmsTimeStamp(
            (ns.seconds_since_epoch & 0xFFFFFFFFL),
            (ns.fraction_of_second & 0xFFFFFFFFL));
    }

    public CmsTimeStamp copy() {
        return new CmsTimeStamp(secondsSinceEpoch, fractional);
    }
}
