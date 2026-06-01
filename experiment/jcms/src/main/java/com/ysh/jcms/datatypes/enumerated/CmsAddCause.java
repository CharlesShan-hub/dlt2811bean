package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsAddCause extends AbstractCmsEnumerated {

    public CmsAddCause() {
        this(0);
    }

    public CmsAddCause(int value) {
        super("AddCause", value, 17);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_add_cause_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAddCause decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_add_cause_decode(data, data.length, v);
        return new CmsAddCause(v.getValue());
    }

    @Override
    public CmsAddCause copy() {
        CmsAddCause clone = new CmsAddCause();
        return copyTo(clone);
    }
}
