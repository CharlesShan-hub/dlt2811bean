package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt8 extends AbstractCmsScalar<Integer> {

    public CmsInt8() {
        super("INT8", 0);
    }

    public CmsInt8(int value) {
        super("INT8", 0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_Int8((byte) (int) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt8 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_decode_Int8(data, data.length, v);
        return new CmsInt8((byte) v.getValue());
    }

    @Override
    public CmsInt8 copy() {
        CmsInt8 clone = new CmsInt8();
        return copyTo(clone);
    }
}
