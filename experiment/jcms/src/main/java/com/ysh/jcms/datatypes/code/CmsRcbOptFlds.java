package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsRcbOptFlds extends AbstractCmsCodedEnum {

    public CmsRcbOptFlds() {
        this(0L);
    }

    public CmsRcbOptFlds(long value) {
        super("RcbOptFlds", value, 10);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_RcbOptFlds(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsRcbOptFlds decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_decode_RcbOptFlds(data, data.length, val);
        return new CmsRcbOptFlds(fromPerBytes(val, 10));
    }

    @Override
    public CmsRcbOptFlds copy() {
        CmsRcbOptFlds clone = new CmsRcbOptFlds();
        return copyTo(clone);
    }
}
