package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsMsvcbOptFlds extends AbstractCmsCodedEnum {

    public CmsMsvcbOptFlds() {
        this(0L);
    }

    public CmsMsvcbOptFlds(long value) {
        super("MsvcbOptFlds", value, 5);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_msvcb_opt_flds_encode(toPerBytes(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsMsvcbOptFlds decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFIDatatypes.INSTANCE.cms_msvcb_opt_flds_decode(data, data.length, val);
        return new CmsMsvcbOptFlds(fromPerBytes(val, 5));
    }

    @Override
    public CmsMsvcbOptFlds copy() {
        CmsMsvcbOptFlds clone = new CmsMsvcbOptFlds();
        return copyTo(clone);
    }
}
