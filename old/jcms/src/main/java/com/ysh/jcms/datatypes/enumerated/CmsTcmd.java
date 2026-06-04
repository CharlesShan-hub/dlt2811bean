package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_tcmd_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 3);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_tcmd_decode(data, data.length, v);
        this.value = v.getValue();
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = (int) PerInteger.decode(pis, 0, size - 1);
    }

    public static CmsTcmd from(byte[] data) {
        return new CmsTcmd().decode(data);
    }
}
