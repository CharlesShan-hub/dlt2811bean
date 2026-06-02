package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

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
        return CmsFFIDatatypes.INSTANCE.cms_int24u_encode(value, buf, outLen);
    }

    public static CmsInt24U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int24u_decode(data, data.length, v);
        return new CmsInt24U(v.getValue());
    }
}
