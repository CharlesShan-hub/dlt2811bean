package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsDbpos extends AbstractCmsCodedEnum {

    public CmsDbpos() {
        this(0);
    }

    public CmsDbpos(long value) {
        super("Dbpos", value, 3);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_dbpos_encode(value.intValue(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsDbpos decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_dbpos_decode(data, data.length, v);
        return new CmsDbpos(v.getValue());
    }

    @Override
    public CmsDbpos copy() {
        CmsDbpos clone = new CmsDbpos();
        return copyTo(clone);
    }
}
