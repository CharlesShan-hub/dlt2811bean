package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt16 extends AbstractCmsScalar<Integer> {

    public CmsInt16() {
        super("INT16", 0);
    }

    public CmsInt16(int value) {
        super("INT16", 0);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_int16_encode((short) (int) value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt16 decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_int16_decode(data, data.length, v);
        return new CmsInt16((short) v.getValue());
    }

    @Override
    public CmsInt16 copy() {
        CmsInt16 clone = new CmsInt16();
        return copyTo(clone);
    }
}
