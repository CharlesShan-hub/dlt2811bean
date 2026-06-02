package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt24U extends AbstractCmsNumeric<Integer> {

    public static final int MIN = 0;
    public static final int MAX = 16777215;

    public CmsInt24U() {
        super("INT24U", MIN, MAX, 0);
    }

    public CmsInt24U(int value) {
        super("INT24U", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_int24u_encode(value, buf, outLen);
    }

    public static CmsInt24U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int24u_decode(data, data.length, v);
        return new CmsInt24U(v.getValue());
    }

    @Override
    public CmsInt24U copy() {
        CmsInt24U clone = new CmsInt24U();
        return copyTo(clone);
    }
}
