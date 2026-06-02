package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.ByteByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt8 extends AbstractCmsNumeric<CmsInt8, Integer> {

    public static final int MIN = -128;
    public static final int MAX = 127;

    public CmsInt8() {
        this(0);
    }

    public CmsInt8(int value) {
        super("INT8", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected int ffiEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int8_encode((byte) (int) value, buf, outLen);
    }

    public static CmsInt8 decode(byte[] data) {
        ByteByReference v = new ByteByReference();
        CmsFFIDatatypes.INSTANCE.cms_int8_decode(data, data.length, v);
        return new CmsInt8(v.getValue());
    }
}
