package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsSmpMod extends AbstractCmsEnumerated<CmsSmpMod> {

    public static final int SAMPLES_PER_NOMINAL_PERIOD = 0;
    public static final int SAMPLES_PER_SECOND         = 1;
    public static final int SECONDS_PER_SAMPLE         = 2;

    public CmsSmpMod() {
        this(0);
    }

    public CmsSmpMod(int value) {
        super("SmpMod", value, 3);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_smp_mod_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 2);
    }

    public static CmsSmpMod decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            IntByReference v = new IntByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_smp_mod_decode(data, data.length, v);
            return new CmsSmpMod(v.getValue());
        }
        return new CmsSmpMod((int) PerInteger.decode(new PerInputStream(data), 0, 2));
    }
}
