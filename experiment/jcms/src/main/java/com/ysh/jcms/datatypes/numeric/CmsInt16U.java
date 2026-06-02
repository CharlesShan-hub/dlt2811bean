package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsInt16U extends AbstractCmsNumeric<Integer> {

    public static final int MIN = 0;
    public static final int MAX = 65535;

    public CmsInt16U() {
        super("INT16U", MIN, MAX, 0);
    }

    public CmsInt16U(int value) {
        super("INT16U", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_int16u_encode(value, buf, outLen);
    }

    public static CmsInt16U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int16u_decode(data, data.length, v);
        return new CmsInt16U(v.getValue());
    }

    @Override
    public CmsInt16U copy() {
        CmsInt16U clone = new CmsInt16U();
        return copyTo(clone);
    }
}
