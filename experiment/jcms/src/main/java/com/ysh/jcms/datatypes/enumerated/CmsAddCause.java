package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;

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
        CmsFFI.INSTANCE.cms_encode_AddCause(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAddCause decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_decode_AddCause(data, data.length, v);
        return new CmsAddCause(v.getValue());
    }

    @Override
    public CmsAddCause copy() {
        CmsAddCause clone = new CmsAddCause();
        return copyTo(clone);
    }
}
