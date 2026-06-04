package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsInt32 extends AbstractCmsNumeric<CmsInt32, Integer> {

    public static final int MIN = -(1 << 31);
    public static final int MAX = (1 << 31) - 1;

    public CmsInt32() {
        this(0);
    }

    public CmsInt32(int value) {
        super("INT32", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int32_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, MIN, MAX);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int32_decode(data, data.length, v);
        this.value = v.getValue();
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = (int) PerInteger.decode(pis, MIN, MAX);
    }

    public static CmsInt32 from(byte[] data) {
        return new CmsInt32().decode(data);
    }
}
