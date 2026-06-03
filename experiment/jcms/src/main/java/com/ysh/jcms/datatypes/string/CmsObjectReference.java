package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import java.nio.charset.StandardCharsets;

public class CmsObjectReference extends CmsVisibleString {

    public CmsObjectReference() {
        super("");
        max(129);
    }

    public CmsObjectReference(String value) {
        this();
        this.value = value != null ? value : "";
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_object_reference_encode(value, buf, outLen);
    }

    @Override
    public CmsObjectReference copy() {
        return (CmsObjectReference) super.copy();
    }

    public static CmsObjectReference decode(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(129);
        CmsFFIDatatypes.INSTANCE.cms_object_reference_decode(data, data.length, strBuf, strLen);
        CmsObjectReference obj = new CmsObjectReference(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
        return obj;
    }
}
