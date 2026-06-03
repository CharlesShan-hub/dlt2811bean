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

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_lcb_opt_flds_decode(data, data.length, val);
        this.value = fromPerBytes(val, 1);
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = fromPerBytes(PerBitString.decodeFixedSizeBytes(pis, 1), 1);
    }

    public static CmsLcbOptFlds from(byte[] data) {
        return new CmsLcbOptFlds().decode(data);
    }
}
