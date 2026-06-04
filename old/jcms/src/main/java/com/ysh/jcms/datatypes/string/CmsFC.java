package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerVisibleString;
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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_fc_encode(value.getBytes(StandardCharsets.US_ASCII), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerVisibleString.encodeFixedSize(pos, value, 2);
    }

    @Override
    public CmsFC copy() {
        return (CmsFC) super.copy();
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] strBuf = new byte[2];
        CmsFFIDatatypes.Holder.INSTANCE.cms_fc_decode(data, data.length, strBuf);
        this.value = new String(strBuf, StandardCharsets.US_ASCII);
        this.present = true;
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = PerVisibleString.decodeFixedSize(pis, 2);
        this.present = true;
    }

    @Override
    public CmsFC decode(byte[] data) {
        return (CmsFC) super.decode(data);
    }

    public static CmsFC from(byte[] data) {
        return new CmsFC().decode(data);
    }
}
