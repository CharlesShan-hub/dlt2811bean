package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsSubReference extends AbstractCmsScalar<String> {

    public CmsSubReference() {
        super("SubReference", "");
    }

    public CmsSubReference(String value) {
        super("SubReference", "");
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_SubReference(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsSubReference decode(byte[] data) {
        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(129);
        CmsFFI.INSTANCE.cms_decode_SubReference(data, data.length, strBuf, strLen);
        return new CmsSubReference(new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII));
    }

    @Override
    public CmsSubReference copy() {
        CmsSubReference clone = new CmsSubReference();
        return copyTo(clone);
    }
}
