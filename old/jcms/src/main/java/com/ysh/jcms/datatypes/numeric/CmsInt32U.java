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

    @Override
    protected void ffiDecode(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int32u_decode(data, data.length, v);
        this.value = v.getValue();
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = PerInteger.decode(pis, MIN, MAX);
    }

    public static CmsInt32U from(byte[] data) {
        return new CmsInt32U().decode(data);
    }
}
