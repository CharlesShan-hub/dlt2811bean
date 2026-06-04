package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_reason_code_encode(toPerBytes(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, toPerBytes(), size);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_reason_code_decode(data, data.length, val);
        this.value = fromPerBytes(val, 7);
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = fromPerBytes(PerBitString.decodeFixedSizeBytes(pis, 7), 7);
    }

    public static CmsReasonCode from(byte[] data) {
        return new CmsReasonCode().decode(data);
    }
}
