package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt16U extends AbstractCmsNumeric<CmsInt16U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = 65535;

    public CmsInt16U() {
        this(0);
    }

    public CmsInt16U(int value) {
        super("INT16U", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int16u_encode(value, buf, outLen);
    }

    public static CmsInt16U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int16u_decode(data, data.length, v);
        return new CmsInt16U(v.getValue());
    }
}
