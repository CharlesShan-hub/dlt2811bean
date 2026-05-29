package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;

public class CmsTriggerConditions extends AbstractCmsCodedEnum {

    public CmsTriggerConditions() {
        this(0L);
    }

    public CmsTriggerConditions(long value) {
        super("TriggerConditions", value, 6);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_TriggerConditions(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTriggerConditions decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFI.INSTANCE.cms_decode_TriggerConditions(data, data.length, val);
        return new CmsTriggerConditions(fromPerBytes(val, 6));
    }

    @Override
    public CmsTriggerConditions copy() {
        CmsTriggerConditions clone = new CmsTriggerConditions();
        return copyTo(clone);
    }
}
