package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

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
        CmsFFIDatatypes.INSTANCE.cms_encode_Quality(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsQuality decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_decode_Quality(data, data.length, val);
        return new CmsQuality(fromPerBytes(val, 13));
    }

    @Override
    public CmsQuality copy() {
        CmsQuality clone = new CmsQuality();
        return copyTo(clone);
    }
}
