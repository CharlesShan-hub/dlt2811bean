package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsDbpos extends AbstractCmsEnumerated<CmsDbpos> {

    public static final int INTERMEDIATE = 0;
    public static final int OFF          = 1;
    public static final int ON           = 2;
    public static final int BAD_STATE    = 3;

    public CmsDbpos() {
        this(INTERMEDIATE);
    }

    public CmsDbpos(int value) {
        super("Dbpos", value, 4);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_dbpos_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 3);
    }

    public static CmsDbpos decode(byte[] data) {
       if (CmsFFIDatatypes.isAvailable()) {
           IntByReference v = new IntByReference();
           CmsFFIDatatypes.Holder.INSTANCE.cms_dbpos_decode(data, data.length, v);
           return new CmsDbpos(v.getValue());
       }
        return new CmsDbpos((int) PerInteger.decode(new PerInputStream(data), 0, 3));
    }
}
