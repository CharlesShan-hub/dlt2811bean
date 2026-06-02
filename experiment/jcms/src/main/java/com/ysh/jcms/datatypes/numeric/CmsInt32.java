package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

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
        return CmsFFIDatatypes.INSTANCE.cms_int32_encode(value, buf, outLen);
    }

    public static CmsInt32 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int32_decode(data, data.length, v);
        return new CmsInt32(v.getValue());
    }
}
