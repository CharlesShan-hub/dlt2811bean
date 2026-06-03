package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import java.nio.charset.StandardCharsets;

public class CmsObjectName extends CmsVisibleString {

    public CmsObjectName() {
        super("");
        max(64);
    }

    public CmsObjectName(String value) {
        this();
        this.value = value != null ? value : "";
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_object_name_encode(value, buf, outLen);
    }

    @Override
    public CmsObjectName copy() {
        return (CmsObjectName) super.copy();
    }

    public static CmsObjectName decode(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference strLen = new IntByReference(64);
        CmsFFIDatatypes.INSTANCE.cms_object_name_decode(data, data.length, strBuf, strLen);
        CmsObjectName obj = new CmsObjectName(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
        return obj;
    }
}
