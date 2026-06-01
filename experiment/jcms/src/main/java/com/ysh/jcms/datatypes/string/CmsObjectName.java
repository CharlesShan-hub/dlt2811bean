package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsObjectName extends AbstractCmsScalar<String> {

    public CmsObjectName() {
        super("ObjectName", "");
    }

    public CmsObjectName(String value) {
        super("ObjectName", "");
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_object_name_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsObjectName decode(byte[] data) {
        byte[] strBuf = new byte[128];
        IntByReference strLen = new IntByReference(64);
        CmsFFIDatatypes.INSTANCE.cms_object_name_decode(data, data.length, strBuf, strLen);
        return new CmsObjectName(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
    }

    @Override
    public CmsObjectName copy() {
        CmsObjectName clone = new CmsObjectName();
        return copyTo(clone);
    }
}
