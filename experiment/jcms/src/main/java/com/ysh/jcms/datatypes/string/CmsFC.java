package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.util.Arrays;

public class CmsFC extends AbstractCmsScalar<CmsFC, byte[]> {

    public CmsFC() {
        super("FC", new byte[2]);
    }

    public CmsFC(byte[] value) {
        super("FC", new byte[2]);
        set(value);
    }

    @Override
    public void set(byte[] value) {
        if (value.length != 2) {
            throw new IllegalArgumentException("FC must be exactly 2 bytes (16 bits)");
        }
        super.set(value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_fc_encode(value, buf, outLen);
    }

    public static CmsFC decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_fc_decode(data, data.length, val);
        return new CmsFC(val);
    }
}
