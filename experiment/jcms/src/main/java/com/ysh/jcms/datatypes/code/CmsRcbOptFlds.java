package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsRcbOptFlds extends AbstractCmsCodedEnum {

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
        this(0L);
    }

    public CmsRcbOptFlds(long value) {
        super("RcbOptFlds", value, 10);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_rcb_opt_flds_encode(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsRcbOptFlds decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_rcb_opt_flds_decode(data, data.length, val);
        return new CmsRcbOptFlds(fromPerBytes(val, 10));
    }

    @Override
    public CmsRcbOptFlds copy() {
        CmsRcbOptFlds clone = new CmsRcbOptFlds();
        return copyTo(clone);
    }
}
