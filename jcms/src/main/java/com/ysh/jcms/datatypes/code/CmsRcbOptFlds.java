package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

public class CmsRcbOptFlds extends AbstractCmsCodedEnum<CmsRcbOptFlds> {

    public static final int RESERVED          = 0;
    public static final int SEQUENCE_NUMBER   = 1;
    public static final int REPORT_TIME_STAMP = 2;
    public static final int REASON_FOR_INCLUSION = 3;
    public static final int DATA_SET_NAME     = 4;
    public static final int DATA_REFERENCE    = 5;
    public static final int BUFFER_OVERFLOW   = 6;
    public static final int ENTRY_ID          = 7;
    public static final int CONF_REVISION     = 8;
    public static final int SEGMENTATION      = 9;

    public CmsRcbOptFlds() {
        this(0);
    }

    public CmsRcbOptFlds(int value) {
        super("RcbOptFlds", value, 10);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_rcb_opt_flds_encode(toPerBytes(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, toPerBytes(), size);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.Holder.INSTANCE.cms_rcb_opt_flds_decode(data, data.length, val);
        this.value = fromPerBytes(val, 10);
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = fromPerBytes(PerBitString.decodeFixedSizeBytes(pis, 10), 10);
    }

    public static CmsRcbOptFlds from(byte[] data) {
        return new CmsRcbOptFlds().decode(data);
    }
}
