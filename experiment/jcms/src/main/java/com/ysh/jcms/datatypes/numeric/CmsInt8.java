package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.ByteByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsInt8 extends AbstractCmsNumeric<CmsInt8, Integer> {

    public static final int MIN = -128;
    public static final int MAX = 127;

    public CmsInt8() {
        this(0);
    }

    public CmsInt8(int value) {
        super("INT8", MIN, MAX, 0);
        set(value);
    }

    @Override
    protected int ffiEncode(byte[] buf, com.sun.jna.ptr.IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int8_encode((byte) (int) value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, MIN, MAX);
    }

    public static CmsInt8 decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            ByteByReference v = new ByteByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_int8_decode(data, data.length, v);
            return new CmsInt8(v.getValue());
        }
        return new CmsInt8((int) PerInteger.decode(new PerInputStream(data), MIN, MAX));
    }
}
