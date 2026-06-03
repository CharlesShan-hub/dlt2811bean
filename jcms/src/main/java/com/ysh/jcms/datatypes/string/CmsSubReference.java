package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerVisibleString;
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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_sub_reference_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerVisibleString.encodeConstrained(pos, value, 0, 129);
    }

    @Override
    public CmsSubReference copy() {
        return (CmsSubReference) super.copy();
    }

    public static CmsSubReference decode(byte[] data) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] strBuf = new byte[256];
           IntByReference strLen = new IntByReference(129);
           CmsFFIDatatypes.Holder.INSTANCE.cms_sub_reference_decode(data, data.length, strBuf, strLen);
           return new CmsSubReference(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
       }
        return new CmsSubReference(PerVisibleString.decodeConstrained(new PerInputStream(data), 0, 129));
    }
}
