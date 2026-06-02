package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsObjectReference extends AbstractCmsScalar<CmsObjectReference, String> {

    public CmsObjectReference() {
        super("ObjectReference", "");
    }

    public CmsObjectReference(String value) {
        super("ObjectReference", "");
        set(value);
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_object_reference_encode(value, buf, outLen);
    }

    public static CmsObjectReference decode(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(129);
        CmsFFIDatatypes.INSTANCE.cms_object_reference_decode(data, data.length, strBuf, strLen);
        return new CmsObjectReference(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
    }
}
