package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt8U extends AbstractCmsNumeric<CmsInt8U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = 255;

    public CmsInt8U() {
        this(0);
    }

    public CmsInt8U(int value) {
        super("INT8U", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int8u_encode((short) (int) value, buf, outLen);
    }

    public static CmsInt8U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int8u_decode(data, data.length, v);
        return new CmsInt8U(v.getValue());
    }
}
