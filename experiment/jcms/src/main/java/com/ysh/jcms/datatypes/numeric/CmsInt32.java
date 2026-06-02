package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt32 extends AbstractCmsNumeric<Integer> {

    public static final int MIN = -(1 << 31);
    public static final int MAX = (1 << 31) - 1;

    public CmsInt32() {
        super("INT32", MIN, MAX, 0);
    }

    public CmsInt32(int value) {
        super("INT32", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_int32_encode(value, buf, outLen);
    }

    public static CmsInt32 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int32_decode(data, data.length, v);
        return new CmsInt32(v.getValue());
    }

    @Override
    public CmsInt32 copy() {
        CmsInt32 clone = new CmsInt32();
        return copyTo(clone);
    }
}
