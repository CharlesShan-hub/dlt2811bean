package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.ByteByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt8 extends AbstractCmsNumeric<Integer> {

    public static final int MIN = -128;
    public static final int MAX = 127;

    public CmsInt8() {
        super("INT8", MIN, MAX, 0);
    }

    public CmsInt8(int value) {
        super("INT8", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_int8_encode((byte) (int) value, buf, outLen);
    }

    public static CmsInt8 decode(byte[] data) {
        ByteByReference v = new ByteByReference();
        CmsFFIDatatypes.INSTANCE.cms_int8_decode(data, data.length, v);
        return new CmsInt8(v.getValue());
    }

    @Override
    public CmsInt8 copy() {
        CmsInt8 clone = new CmsInt8();
        return copyTo(clone);
    }
}
