package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

public class CmsLcbOptFlds extends AbstractCmsCodedEnum<CmsLcbOptFlds> {

    /** Bit 0 — PURGE */
    public static final int PURGE = 0;

    public CmsLcbOptFlds() {
        this(0);
    }

    public CmsLcbOptFlds(int value) {
        super("LcbOptFlds", value, 1);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_lcb_opt_flds_encode(toPerBytes(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, toPerBytes(), size);
    }

    public static CmsLcbOptFlds decode(byte[] data) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] val = new byte[1];
           CmsFFIDatatypes.Holder.INSTANCE.cms_lcb_opt_flds_decode(data, data.length, val);
           return new CmsLcbOptFlds(fromPerBytes(val, 1));
       }
        return new CmsLcbOptFlds(fromPerBytes(PerBitString.decodeFixedSizeBytes(new PerInputStream(data), 1), 1));
    }
}
