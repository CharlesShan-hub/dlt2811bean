package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt16 extends AbstractCmsNumeric<Integer> {

    public static final int MIN = -32768;
    public static final int MAX = 32767;

    public CmsInt16() {
        super("INT16", MIN, MAX, 0);
    }

    public CmsInt16(int value) {
        super("INT16", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_int16_encode((short) (int) value, buf, outLen);
    }

    public static CmsInt16 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int16_decode(data, data.length, v);
        return new CmsInt16(v.getValue());
    }

    @Override
    public CmsInt16 copy() {
        CmsInt16 clone = new CmsInt16();
        return copyTo(clone);
    }
}
