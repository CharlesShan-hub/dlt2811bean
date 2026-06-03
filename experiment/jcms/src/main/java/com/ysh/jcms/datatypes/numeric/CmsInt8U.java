package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsInt8U extends AbstractCmsNumeric<CmsInt8U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = 255;

    public CmsInt8U() {
        this(0);
    }

    public CmsInt8U(int value) {
        super("INT8U", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int8u_encode((short) (int) value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, MIN, MAX);
    }

    public static CmsInt8U decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            IntByReference v = new IntByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_int8u_decode(data, data.length, v);
            return new CmsInt8U(v.getValue());
        }
        return new CmsInt8U((int) PerInteger.decode(new PerInputStream(data), MIN, MAX));
    }
}
