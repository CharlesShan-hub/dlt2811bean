package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
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
        CmsFFIDatatypes.INSTANCE.cms_int8_encode((byte) (int) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt8 decode(byte[] data) {
        ByteByReference v = new ByteByReference();
        CmsFFIDatatypes.INSTANCE.cms_int8_decode(data, data.length, v);
        return new CmsInt8((int) v.getValue());
    }

    @Override
    public CmsInt8 copy() {
        CmsInt8 clone = new CmsInt8();
        return copyTo(clone);
    }
}
