package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
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
        CmsFFIDatatypes.INSTANCE.cms_int32_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt32 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int32_decode(data, data.length, v);
        return new CmsInt32(v.getValue());
    }

    @Override
    public CmsInt32 copy() {
        CmsInt32 clone = new CmsInt32();
        return copyTo(clone);
    }
}
