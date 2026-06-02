package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsFloat64 extends AbstractCmsNumeric<CmsFloat64, Double> {

    public CmsFloat64() {
        this(0.0);
    }

    public CmsFloat64(double value) {
        super("FLOAT64", value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_float64_encode(value, buf, outLen);
    }

    public static CmsFloat64 decode(byte[] data) {
        double[] v = new double[1];
        CmsFFIDatatypes.INSTANCE.cms_float64_decode(data, data.length, v);
        return new CmsFloat64(v[0]);
    }
}
