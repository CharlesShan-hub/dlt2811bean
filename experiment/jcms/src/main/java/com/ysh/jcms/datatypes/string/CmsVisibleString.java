package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsVisibleString extends AbstractCmsScalar<String> {

    public CmsVisibleString() {
        super("VisibleString", "");
    }

    public CmsVisibleString(String value) {
        super("VisibleString", "");
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_VisibleString(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsVisibleString decode(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(255);
        CmsFFIDatatypes.INSTANCE.cms_decode_VisibleString(data, data.length, strBuf, strLen);
        return new CmsVisibleString(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
    }

    @Override
    public CmsVisibleString copy() {
        CmsVisibleString clone = new CmsVisibleString();
        return copyTo(clone);
    }
}
