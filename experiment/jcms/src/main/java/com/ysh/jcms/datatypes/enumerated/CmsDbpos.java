package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsDbpos extends AbstractCmsEnumerated<CmsDbpos> {

    public static final int INTERMEDIATE = 0;
    public static final int OFF          = 1;
    public static final int ON           = 2;
    public static final int BAD_STATE    = 3;

    public CmsDbpos() {
        this(INTERMEDIATE);
    }

    public CmsDbpos(int value) {
        super("Dbpos", value, 4);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_dbpos_encode(value, buf, outLen);
    }

    public static CmsDbpos decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_dbpos_decode(data, data.length, v);
        return new CmsDbpos(v.getValue());
    }
}
