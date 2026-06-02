package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsObjectName extends AbstractCmsScalar<CmsObjectName, String> {

    public CmsObjectName() {
        super("ObjectName", "");
    }

    public CmsObjectName(String value) {
        super("ObjectName", "");
        set(value);
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_object_name_encode(value, buf, outLen);
    }

    public static CmsObjectName decode(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference strLen = new IntByReference(64);
        CmsFFIDatatypes.INSTANCE.cms_object_name_decode(data, data.length, strBuf, strLen);
        return new CmsObjectName(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
    }
}
