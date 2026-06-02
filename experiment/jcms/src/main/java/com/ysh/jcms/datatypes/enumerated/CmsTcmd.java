package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsTcmd extends AbstractCmsEnumerated<CmsTcmd> {

    public static final int RESERVED = 0;
    public static final int SELECT   = 1;
    public static final int OPERATE  = 2;
    public static final int CANCEL   = 3;

    public CmsTcmd() {
        this(RESERVED);
    }

    public CmsTcmd(int value) {
        super("Tcmd", value, 4);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_tcmd_encode(value, buf, outLen);
    }

    public static CmsTcmd decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_tcmd_decode(data, data.length, v);
        return new CmsTcmd(v.getValue());
    }
}
