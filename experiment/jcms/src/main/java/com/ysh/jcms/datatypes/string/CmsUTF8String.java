package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsUTF8String extends AbstractCmsScalar<String> {

    public CmsUTF8String() {
        super("UTF8String", "");
    }

    public CmsUTF8String(String value) {
        super("UTF8String", "");
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        byte[] utf8Bytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] nullTerminated = new byte[utf8Bytes.length + 1];
        System.arraycopy(utf8Bytes, 0, nullTerminated, 0, utf8Bytes.length);
        nullTerminated[utf8Bytes.length] = 0;
        CmsFFIDatatypes.INSTANCE.cms_encode_UTF8String(nullTerminated, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsUTF8String decode(byte[] data) {
        byte[] strBuf = new byte[512];
        java.util.Arrays.fill(strBuf, (byte) 0);
        IntByReference strLen = new IntByReference(255);
        CmsFFIDatatypes.INSTANCE.cms_decode_UTF8String(data, data.length, strBuf, strLen);
        return new CmsUTF8String(new String(strBuf, 0, strLen.getValue(), StandardCharsets.UTF_8));
    }

    @Override
    public CmsUTF8String copy() {
        CmsUTF8String clone = new CmsUTF8String();
        return copyTo(clone);
    }
}
