package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsLcbOptFlds extends AbstractCmsCodedEnum<CmsLcbOptFlds> {

    public static final int PURGE = 0;

    public CmsLcbOptFlds() {
        this(0L);
    }

    public CmsLcbOptFlds(long value) {
        super("LcbOptFlds", value, 1);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_lcb_opt_flds_encode(toPerBytes(), buf, outLen);
    }

    public static CmsLcbOptFlds decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_lcb_opt_flds_decode(data, data.length, val);
        return new CmsLcbOptFlds(fromPerBytes(val, 1));
    }
}
