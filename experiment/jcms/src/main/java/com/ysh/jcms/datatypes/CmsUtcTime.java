package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsUtcTime {

    private final long secondsSinceEpoch;
    private final int fractional;

    public CmsUtcTime(long secondsSinceEpoch, int fractional) {
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.fractional = fractional;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_UtcTime(secondsSinceEpoch, fractional, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsUtcTime decode(byte[] data) {
        LongByReference sec = new LongByReference();
        IntByReference frac = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_UtcTime(data, data.length, sec, frac);
        return new CmsUtcTime(sec.getValue(), frac.getValue());
    }
}
