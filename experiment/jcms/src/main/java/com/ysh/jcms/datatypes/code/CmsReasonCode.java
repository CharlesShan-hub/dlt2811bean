package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsReasonCode extends AbstractCmsCodedEnum<CmsReasonCode> {

    public static final int RESERVED              = 0;
    public static final int DATA_CHANGE           = 1;
    public static final int QUALITY_CHANGE        = 2;
    public static final int DATA_UPDATE           = 3;
    public static final int INTEGRITY             = 4;
    public static final int GENERAL_INTERROGATION = 5;
    public static final int APPLICATION_TRIGGER   = 6;

    public CmsReasonCode() {
        this(0);
    }

    public CmsReasonCode(int value) {
        super("ReasonCode", value, 7);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_reason_code_encode(toPerBytes(), buf, outLen);
    }

    public static CmsReasonCode decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_reason_code_decode(data, data.length, val);
        return new CmsReasonCode(fromPerBytes(val, 7));
    }
}
