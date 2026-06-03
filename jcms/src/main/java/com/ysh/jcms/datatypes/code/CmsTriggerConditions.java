package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

public class CmsTriggerConditions extends AbstractCmsCodedEnum<CmsTriggerConditions> {

    public static final int RESERVED              = 0;
    public static final int DATA_CHANGE           = 1;
    public static final int QUALITY_CHANGE        = 2;
    public static final int DATA_UPDATE           = 3;
    public static final int INTEGRITY             = 4;
    public static final int GENERAL_INTERROGATION = 5;

    public CmsTriggerConditions() {
        this(0);
    }

    public CmsTriggerConditions(int value) {
        super("TriggerConditions", value, 6);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_trigger_conditions_encode(toPerBytes(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, toPerBytes(), size);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_trigger_conditions_decode(data, data.length, val);
        this.value = fromPerBytes(val, 6);
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = fromPerBytes(PerBitString.decodeFixedSizeBytes(pis, 6), 6);
    }

    public static CmsTriggerConditions from(byte[] data) {
        return new CmsTriggerConditions().decode(data);
    }
}
