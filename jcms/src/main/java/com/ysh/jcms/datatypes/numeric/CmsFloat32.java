package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;

public class CmsFloat32 extends AbstractCmsNumeric<CmsFloat32, Float> {

    public CmsFloat32() {
        this(0.0f);
    }

    public CmsFloat32(float value) {
        super("FLOAT32", value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_float32_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        pos.writeSignedInteger(Float.floatToIntBits(value), 4);
    }

    public static CmsFloat32 decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            float[] v = new float[1];
            CmsFFIDatatypes.Holder.INSTANCE.cms_float32_decode(data, data.length, v);
            return new CmsFloat32(v[0]);
        }
        return new CmsFloat32(Float.intBitsToFloat((int) new PerInputStream(data).readSignedInteger(4)));
    }
}
