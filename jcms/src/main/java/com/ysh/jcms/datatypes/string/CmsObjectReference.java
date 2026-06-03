package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerVisibleString;
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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_object_reference_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerVisibleString.encodeConstrained(pos, value, 0, 129);
    }

    @Override
    public CmsObjectReference copy() {
        return (CmsObjectReference) super.copy();
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference strLen = new IntByReference(64);
        CmsFFIDatatypes.Holder.INSTANCE.cms_object_reference_decode(data, data.length, strBuf, strLen);
        this.value = new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII);
        this.present = true;
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = PerVisibleString.decodeConstrained(pis, 0, 64);
        this.present = true;
    }

    @Override
    public CmsObjectReference decode(byte[] data) {
        return (CmsObjectReference) super.decode(data);
    }

    public static CmsObjectReference from(byte[] data) {
        return new CmsObjectReference().decode(data);
    }
}
