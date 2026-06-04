package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

public class CmsFloat64 extends AbstractCmsNumeric<CmsFloat64, Double> {

    public CmsFloat64() {
        this(0.0);
    }

    public CmsFloat64(double value) {
        super("FLOAT64", value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_float64_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        pos.writeSignedInteger(Double.doubleToLongBits(value), 8);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        double[] v = new double[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_float64_decode(data, data.length, v);
        this.value = v[0];
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = Double.longBitsToDouble(pis.readSignedInteger(8));
    }

    public static CmsFloat64 from(byte[] data) {
        return new CmsFloat64().decode(data);
    }
}
