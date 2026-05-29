package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsFloat64 extends AbstractCmsScalar<Double> {

    public CmsFloat64() {
        super("FLOAT64", 0.0);
    }

    public CmsFloat64(double value) {
        super("FLOAT64", 0.0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_Float64(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFloat64 decode(byte[] data) {
        double[] v = new double[1];
        CmsFFIDatatypes.INSTANCE.cms_decode_Float64(data, data.length, v);
        return new CmsFloat64(v[0]);
    }

    @Override
    public CmsFloat64 copy() {
        CmsFloat64 clone = new CmsFloat64();
        return copyTo(clone);
    }
}
