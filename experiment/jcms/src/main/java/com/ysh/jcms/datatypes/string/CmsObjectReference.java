package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsObjectReference extends AbstractCmsScalar<String> {

    public CmsObjectReference() {
        super("ObjectReference", "");
    }

    public CmsObjectReference(String value) {
        super("ObjectReference", "");
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_object_reference_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsObjectReference decode(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(129);
        CmsFFIDatatypes.INSTANCE.cms_object_reference_decode(data, data.length, strBuf, strLen);
        return new CmsObjectReference(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
    }

    @Override
    public CmsObjectReference copy() {
        CmsObjectReference clone = new CmsObjectReference();
        return copyTo(clone);
    }
}
