package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class CmsTimeStamp {

    private final long secondsSinceEpoch;
    private final long fractional;

    public CmsTimeStamp(long secondsSinceEpoch, long fractional) {
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractional = fractional;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_TimeStamp(secondsSinceEpoch, fractional, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTimeStamp decode(byte[] data) {
        LongByReference sec = new LongByReference();
        LongByReference frac = new LongByReference();
        CmsFFI.INSTANCE.cms_decode_TimeStamp(data, data.length, sec, frac);
        return new CmsTimeStamp(sec.getValue(), frac.getValue());
    }

    @Override
    public String toString() {
        return secondsSinceEpoch + "." + fractional;
    }
}
