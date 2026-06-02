package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsMsvcbOptFlds extends AbstractCmsCodedEnum<CmsMsvcbOptFlds> {

    public static final int REFRESH_TIME  = 0;
    public static final int RESERVED      = 1;
    public static final int SAMPLE_RATE   = 2;
    public static final int DATA_SET_NAME = 3;
    public static final int SECURITY      = 4;

    public CmsMsvcbOptFlds() {
        this(0L);
    }

    public CmsMsvcbOptFlds(long value) {
        super("MsvcbOptFlds", value, 5);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_msvcb_opt_flds_encode(toPerBytes(), buf, outLen);
    }

    public static CmsMsvcbOptFlds decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_msvcb_opt_flds_decode(data, data.length, val);
        return new CmsMsvcbOptFlds(fromPerBytes(val, 5));
    }
}
