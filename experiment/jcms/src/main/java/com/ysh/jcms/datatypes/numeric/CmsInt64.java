package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt64 extends AbstractCmsNumeric<CmsInt64, Long> {

    public static final long MIN = Long.MIN_VALUE;
    public static final long MAX = Long.MAX_VALUE;

    public CmsInt64() {
        this(0L);
    }

    public CmsInt64(long value) {
        super("INT64", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int64_encode(value, buf, outLen);
    }

    public static CmsInt64 decode(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_int64_decode(data, data.length, v);
        return new CmsInt64(v.getValue());
    }
}
