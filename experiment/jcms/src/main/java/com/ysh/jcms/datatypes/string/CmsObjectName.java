package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerVisibleString;
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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_object_name_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerVisibleString.encodeConstrained(pos, value, 0, 64);
    }

    @Override
    public CmsObjectName copy() {
        return (CmsObjectName) super.copy();
    }

    public static CmsObjectName decode(byte[] data) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] strBuf = new byte[128];
           IntByReference strLen = new IntByReference(64);
           CmsFFIDatatypes.Holder.INSTANCE.cms_object_name_decode(data, data.length, strBuf, strLen);
           return new CmsObjectName(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
       }
        return new CmsObjectName(PerVisibleString.decodeConstrained(new PerInputStream(data), 0, 64));
    }
}
