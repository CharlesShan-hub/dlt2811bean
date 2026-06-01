package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt24U extends AbstractCmsScalar<Integer> {

    public CmsInt24U() {
        super("INT24U", 0);
    }

    public CmsInt24U(int value) {
        super("INT24U", 0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_int24u_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt24U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int24u_decode(data, data.length, v);
        return new CmsInt24U(v.getValue());
    }

    @Override
    public CmsInt24U copy() {
        CmsInt24U clone = new CmsInt24U();
        return copyTo(clone);
    }
}
