package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt16 extends AbstractCmsNumeric<CmsInt16, Integer> {

    public static final int MIN = -32768;
    public static final int MAX = 32767;

    public CmsInt16() {
        this(0);
    }

    public CmsInt16(int value) {
        super("INT16", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int16_encode((short) (int) value, buf, outLen);
    }

    public static CmsInt16 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int16_decode(data, data.length, v);
        return new CmsInt16((short) v.getValue());
    }
}
