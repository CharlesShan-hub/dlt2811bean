package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt8U extends AbstractCmsScalar<Integer> {

    public CmsInt8U() {
        super("INT8U", 0);
    }

    public CmsInt8U(int value) {
        super("INT8U", 0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_Int8U((short) (int) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt8U decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_decode_Int8U(data, data.length, v);
        return new CmsInt8U(v.getValue());
    }

    @Override
    public CmsInt8U copy() {
        CmsInt8U clone = new CmsInt8U();
        return copyTo(clone);
    }
}
