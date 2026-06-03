package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import java.nio.charset.StandardCharsets;

public class CmsSubReference extends CmsVisibleString {

    public CmsSubReference() {
        super("");
        max(129);
    }

    public CmsSubReference(String value) {
        this();
        this.value = value != null ? value : "";
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_sub_reference_encode(value, buf, outLen);
    }

    @Override
    public CmsSubReference copy() {
        return (CmsSubReference) super.copy();
    }

    public static CmsSubReference decode(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(129);
        CmsFFIDatatypes.INSTANCE.cms_sub_reference_decode(data, data.length, strBuf, strLen);
        CmsSubReference obj = new CmsSubReference(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
        return obj;
    }
}
