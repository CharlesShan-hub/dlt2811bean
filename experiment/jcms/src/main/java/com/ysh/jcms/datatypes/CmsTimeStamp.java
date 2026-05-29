package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
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
        CmsFFI.INSTANCE.cms_ffi_encode_TimeStamp(secondsSinceEpoch, fractional, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTimeStamp decode(byte[] data) {
        LongByReference sec = new LongByReference();
        LongByReference frac = new LongByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_TimeStamp(data, data.length, sec, frac);
        return new CmsTimeStamp(sec.getValue(), frac.getValue());
    }
}
