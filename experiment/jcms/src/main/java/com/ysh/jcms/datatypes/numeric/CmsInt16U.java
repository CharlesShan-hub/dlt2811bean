package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt16U extends AbstractCmsScalar<Integer> {

    public CmsInt16U() {
        super("INT16U", 0);
    }

    public CmsInt16U(int value) {
        super("INT16U", 0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_int16u_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
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
