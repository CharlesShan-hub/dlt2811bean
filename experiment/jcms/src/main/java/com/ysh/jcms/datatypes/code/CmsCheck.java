package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;

public class CmsCheck extends AbstractCmsCodedEnum {

    public CmsCheck() {
        this(0L);
    }

    public CmsCheck(long value) {
        super("Check", value, 16);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_Check(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsCheck decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFI.INSTANCE.cms_decode_Check(data, data.length, val);
        return new CmsCheck(fromPerBytes(val, 16));
    }

    @Override
    public CmsCheck copy() {
        CmsCheck clone = new CmsCheck();
        return copyTo(clone);
    }
}
