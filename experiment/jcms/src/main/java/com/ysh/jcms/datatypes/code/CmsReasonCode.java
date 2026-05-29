package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;

public class CmsReasonCode extends AbstractCmsCodedEnum {

    public CmsReasonCode() {
        this(0L);
    }

    public CmsReasonCode(long value) {
        super("ReasonCode", value, 7);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_ReasonCode(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsReasonCode decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFI.INSTANCE.cms_decode_ReasonCode(data, data.length, val);
        return new CmsReasonCode(fromPerBytes(val, 7));
    }

    @Override
    public CmsReasonCode copy() {
        CmsReasonCode clone = new CmsReasonCode();
        return copyTo(clone);
    }
}
