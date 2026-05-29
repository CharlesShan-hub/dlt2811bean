package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsFloat32 extends AbstractCmsScalar<Float> {

    public CmsFloat32() {
        super("FLOAT32", 0.0f);
    }

    public CmsFloat32(float value) {
        super("FLOAT32", 0.0f);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_Float32(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFloat32 decode(byte[] data) {
        float[] v = new float[1];
        CmsFFIDatatypes.INSTANCE.cms_decode_Float32(data, data.length, v);
        return new CmsFloat32(v[0]);
    }

    @Override
    public CmsFloat32 copy() {
        CmsFloat32 clone = new CmsFloat32();
        return copyTo(clone);
    }
}
