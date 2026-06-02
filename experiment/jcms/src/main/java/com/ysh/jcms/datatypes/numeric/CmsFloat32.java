package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsFloat32 extends AbstractCmsNumeric<Float> {

    public CmsFloat32() {
        super("FLOAT32", 0.0f);
    }

    public CmsFloat32(float value) {
        super("FLOAT32", 0.0f);
        set(value);
    }

    @Override
    protected void doEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        CmsFFIDatatypes.INSTANCE.cms_float32_encode(value, buf, outLen);
    }

    public static CmsFloat32 decode(byte[] data) {
        float[] v = new float[1];
        CmsFFIDatatypes.INSTANCE.cms_float32_decode(data, data.length, v);
        return new CmsFloat32(v[0]);
    }

    @Override
    public CmsFloat32 copy() {
        CmsFloat32 clone = new CmsFloat32();
        return copyTo(clone);
    }
}
