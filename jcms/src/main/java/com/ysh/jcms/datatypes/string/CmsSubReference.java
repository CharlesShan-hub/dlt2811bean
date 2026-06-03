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

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference strLen = new IntByReference(129);
        CmsFFIDatatypes.Holder.INSTANCE.cms_sub_reference_decode(data, data.length, strBuf, strLen);
        this.value = new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII);
        this.present = true;
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = PerVisibleString.decodeConstrained(pis, 0, 129);
        this.present = true;
    }

    @Override
    public CmsSubReference decode(byte[] data) {
        return (CmsSubReference) super.decode(data);
    }

    public static CmsSubReference from(byte[] data) {
        return new CmsSubReference().decode(data);
    }
}
