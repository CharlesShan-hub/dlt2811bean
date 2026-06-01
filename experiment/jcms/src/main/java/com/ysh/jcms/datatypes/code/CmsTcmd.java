package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsTcmd extends AbstractCmsCodedEnum {

    public CmsTcmd() {
        this(0);
    }

    public CmsTcmd(long value) {
        super("Tcmd", value, 3);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_tcmd_encode(value.intValue(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTcmd decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_tcmd_decode(data, data.length, v);
        return new CmsTcmd(v.getValue());
    }

    @Override
    public CmsTcmd copy() {
        CmsTcmd clone = new CmsTcmd();
        return copyTo(clone);
    }
}
