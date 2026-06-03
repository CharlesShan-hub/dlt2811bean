package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsInt24U extends AbstractCmsNumeric<CmsInt24U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = 16777215;

    public CmsInt24U() {
        this(0);
    }

    public CmsInt24U(int value) {
        super("INT24U", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int24u_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, MIN, MAX);
    }

    public static CmsInt24U decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            IntByReference v = new IntByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_int24u_decode(data, data.length, v);
            return new CmsInt24U(v.getValue());
        }
        return new CmsInt24U((int) PerInteger.decode(new PerInputStream(data), MIN, MAX));
    }
}
