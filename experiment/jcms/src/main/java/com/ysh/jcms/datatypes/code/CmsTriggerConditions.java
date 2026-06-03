package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

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
        return CmsFFIDatatypes.INSTANCE.cms_trigger_conditions_encode(toPerBytes(), buf, outLen);
    }

    public static CmsTriggerConditions decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_trigger_conditions_decode(data, data.length, val);
        return new CmsTriggerConditions(fromPerBytes(val, 6));
    }
}
