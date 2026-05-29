package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.CmsFFI;

public class CmsUtcTime extends AbstractCmsCompound {

    private final long secondsSinceEpoch;

    public CmsUtcTime(long secondsSinceEpoch) {
        super("UtcTime");
        this.secondsSinceEpoch = secondsSinceEpoch;
    }

    public long getSecondsSinceEpoch() { return secondsSinceEpoch; }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_UtcTime(secondsSinceEpoch, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsUtcTime decode(byte[] data) {
        LongByReference sec = new LongByReference();
        CmsFFI.INSTANCE.cms_decode_UtcTime(data, data.length, sec);
        return new CmsUtcTime(sec.getValue());
    }

    public CmsUtcTime copy() {
        return new CmsUtcTime(secondsSinceEpoch);
    }
}
