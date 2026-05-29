package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt32 extends AbstractCmsScalar<Integer> {

    public CmsInt32() {
        super("INT32", 0);
    }

    public CmsInt32(int value) {
        super("INT32", 0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_Int32(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt32 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_decode_Int32(data, data.length, v);
        return new CmsInt32(v.getValue());
    }

    @Override
    public CmsInt32 copy() {
        CmsInt32 clone = new CmsInt32();
        return copyTo(clone);
    }
}
