package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int64_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encodeUnconstrained(pos, value);
    }

    public static CmsInt64 decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            LongByReference v = new LongByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_int64_decode(data, data.length, v);
            return new CmsInt64(v.getValue());
        }
        return new CmsInt64(PerInteger.decodeUnconstrained(new PerInputStream(data)));
    }
}
