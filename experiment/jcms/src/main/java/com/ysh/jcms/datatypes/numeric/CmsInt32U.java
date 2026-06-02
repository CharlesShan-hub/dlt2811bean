package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt32U extends AbstractCmsNumeric<CmsInt32U, Long> {

    public static final long MIN = 0L;
    public static final long MAX = 4294967295L;

    public CmsInt32U() {
        this(0L);
    }

    public CmsInt32U(long value) {
        super("INT32U", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int32u_encode(value, buf, outLen);
    }

    public static CmsInt32U decode(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_int32u_decode(data, data.length, v);
        return new CmsInt32U(v.getValue());
    }
}
