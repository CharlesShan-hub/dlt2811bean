package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsInt16 extends AbstractCmsNumeric<CmsInt16, Integer> {

    public static final int MIN = -32768;
    public static final int MAX = 32767;

    public CmsInt16() {
        this(0);
    }

    public CmsInt16(int value) {
        super("INT16", MIN, MAX, value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int16_encode((short) (int) value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, MIN, MAX);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int16_decode(data, data.length, v);
        this.value = (int) (short) v.getValue();
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = (int) PerInteger.decode(pis, MIN, MAX);
    }

    public static CmsInt16 from(byte[] data) {
        return new CmsInt16().decode(data);
    }
}
