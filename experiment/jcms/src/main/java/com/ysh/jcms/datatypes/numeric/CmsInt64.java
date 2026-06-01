package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsInt64 extends AbstractCmsScalar<Long> {

    public CmsInt64() {
        super("INT64", 0L);
    }

    public CmsInt64(long value) {
        super("INT64", 0L);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_int64_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt64 decode(byte[] data) {
        LongByReference v = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_int64_decode(data, data.length, v);
        return new CmsInt64(v.getValue());
    }

    @Override
    public CmsInt64 copy() {
        CmsInt64 clone = new CmsInt64();
        return copyTo(clone);
    }
}
