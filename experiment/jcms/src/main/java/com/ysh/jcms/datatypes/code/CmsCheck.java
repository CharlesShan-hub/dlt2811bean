package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsCheck extends AbstractCmsCodedEnum<CmsCheck> {

    public CmsCheck() {
        this(0L);
    }

    public CmsCheck(long value) {
        super("Check", value, 2);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_check_encode(toPerBytes(), buf, outLen);
    }

    public static CmsCheck decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_check_decode(data, data.length, val);
        return new CmsCheck(fromPerBytes(val, 2));
    }
}
