package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;

public class CmsQuality extends AbstractCmsCodedEnum {

    public CmsQuality() {
        this(0L);
    }

    public CmsQuality(long value) {
        super("Quality", value, 13);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_Quality(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsQuality decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFI.INSTANCE.cms_decode_Quality(data, data.length, val);
        return new CmsQuality(fromPerBytes(val, 13));
    }

    @Override
    public CmsQuality copy() {
        CmsQuality clone = new CmsQuality();
        return copyTo(clone);
    }
}
