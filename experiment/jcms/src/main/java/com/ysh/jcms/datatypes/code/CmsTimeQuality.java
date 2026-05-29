package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;

public class CmsTimeQuality extends AbstractCmsCodedEnum {

    public CmsTimeQuality() {
        this(0L);
    }

    public CmsTimeQuality(long value) {
        super("TimeQuality", value, 3);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_TimeQuality(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTimeQuality decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFI.INSTANCE.cms_decode_TimeQuality(data, data.length, val);
        return new CmsTimeQuality(fromPerBytes(val, 3));
    }

    @Override
    public CmsTimeQuality copy() {
        CmsTimeQuality clone = new CmsTimeQuality();
        return copyTo(clone);
    }
}
