package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import java.nio.charset.StandardCharsets;

public class CmsFC extends CmsVisibleString {

    public CmsFC() {
        super("");
        size(2);
    }

    public CmsFC(String value) {
        this();
        if (value.length() != 2) {
            throw new IllegalArgumentException("FC must be exactly 2 characters");
        }
        this.value = value;
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_fc_encode(value.getBytes(StandardCharsets.US_ASCII), buf, outLen);
    }

    @Override
    public CmsFC copy() {
        return (CmsFC) super.copy();
    }

    public static CmsFC decode(byte[] data) {
        byte[] strBuf = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_fc_decode(data, data.length, strBuf);
        CmsFC fc = new CmsFC(new String(strBuf, StandardCharsets.US_ASCII));
        return fc;
    }
}
