package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int32u_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, MIN, MAX);
    }

    public static CmsInt32U decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            LongByReference v = new LongByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_int32u_decode(data, data.length, v);
            return new CmsInt32U(v.getValue());
        }
        return new CmsInt32U(PerInteger.decode(new PerInputStream(data), MIN, MAX));
    }
}
