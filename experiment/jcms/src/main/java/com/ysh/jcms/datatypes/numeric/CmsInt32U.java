package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt32U extends AbstractCmsScalar<Long> {

    public CmsInt32U() {
        super("INT32U", 0L);
    }

    public CmsInt32U(long value) {
        super("INT32U", 0L);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_Int32U(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt32U decode(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_Int32U(data, data.length, v);
        return new CmsInt32U(v.getValue());
    }

    @Override
    public CmsInt32U copy() {
        CmsInt32U clone = new CmsInt32U();
        return copyTo(clone);
    }
}
