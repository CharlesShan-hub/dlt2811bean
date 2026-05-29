package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsTimeStamp extends AbstractCmsCompound {

    private final long secondsSinceEpoch;
    private final long fractional;

    public CmsTimeStamp(long secondsSinceEpoch, long fractional) {
        super("TimeStamp");
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractional = fractional;
    }

    public long getSecondsSinceEpoch() { return secondsSinceEpoch; }
    public long getFractional() { return fractional; }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_TimeStamp(secondsSinceEpoch, fractional, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTimeStamp decode(byte[] data) {
        LongByReference sec = new LongByReference();
        LongByReference frac = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_TimeStamp(data, data.length, sec, frac);
        return new CmsTimeStamp(sec.getValue(), frac.getValue());
    }

    public CmsTimeStamp copy() {
        return new CmsTimeStamp(secondsSinceEpoch, fractional);
    }
}
