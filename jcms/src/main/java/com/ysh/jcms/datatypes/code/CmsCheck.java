package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

public class CmsCheck extends AbstractCmsCodedEnum<CmsCheck> {

    // ==================== Bit positions ====================
    /** Bit 0 — synchrocheck */
    public static final int SYNCHROCHECK = 0;
    /** Bit 1 — interlock-check */
    public static final int INTERLOCK_CHECK = 1;

    public CmsCheck() {
        this(0);
    }

    public CmsCheck(int value) {
        super("Check", value, 2);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_check_encode(toPerBytes(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, toPerBytes(), size);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_check_decode(data, data.length, val);
        this.value = fromPerBytes(val, 2);
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = fromPerBytes(PerBitString.decodeFixedSizeBytes(pis, 2), 2);
    }

    public static CmsCheck from(byte[] data) {
        return new CmsCheck().decode(data);
    }
}
