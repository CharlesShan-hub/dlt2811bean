package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsLcbOptFlds extends AbstractCmsCodedEnum {

    public CmsLcbOptFlds() {
        this(0L);
    }

    public CmsLcbOptFlds(long value) {
        super("LcbOptFlds", value, 1);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        byte[] bytes = toPerBytes();
        CmsFFIDatatypes.INSTANCE.cms_encode_LcbOptFlds(bytes, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsLcbOptFlds decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_decode_LcbOptFlds(data, data.length, val);
        return new CmsLcbOptFlds(fromPerBytes(val, 1));
    }

    @Override
    public CmsLcbOptFlds copy() {
        CmsLcbOptFlds clone = new CmsLcbOptFlds();
        return copyTo(clone);
    }
}
