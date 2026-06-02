package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsFloat64 extends AbstractCmsNumeric<Double> {

    public CmsFloat64() {
        super("FLOAT64", 0.0);
    }

    public CmsFloat64(double value) {
        super("FLOAT64", 0.0);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_float64_encode(value, buf, outLen);
    }

    public static CmsFloat64 decode(byte[] data) {
        double[] v = new double[1];
        CmsFFIDatatypes.INSTANCE.cms_float64_decode(data, data.length, v);
        return new CmsFloat64(v[0]);
    }

    @Override
    public CmsFloat64 copy() {
        CmsFloat64 clone = new CmsFloat64();
        return copyTo(clone);
    }
}
