package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.nio.charset.StandardCharsets;

public class CmsUTF8String extends AbstractCmsScalar<CmsUTF8String, String> {

    public CmsUTF8String() {
        super("UTF8String", "");
    }

    public CmsUTF8String(String value) {
        super("UTF8String", "");
        set(value);
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        byte[] utf8Bytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] nullTerminated = new byte[utf8Bytes.length + 1];
        System.arraycopy(utf8Bytes, 0, nullTerminated, 0, utf8Bytes.length);
        nullTerminated[utf8Bytes.length] = 0;
        return CmsFFIDatatypes.INSTANCE.cms_utf8_string_encode(nullTerminated, 255, buf, outLen);
    }

    public static CmsUTF8String decode(byte[] data) {
        byte[] strBuf = new byte[512];
        java.util.Arrays.fill(strBuf, (byte) 0);
        IntByReference strLen = new IntByReference(255);
        CmsFFIDatatypes.INSTANCE.cms_utf8_string_decode(data, data.length, 255, strBuf, strLen);
        return new CmsUTF8String(new String(strBuf, 0, strLen.getValue(), StandardCharsets.UTF_8));
    }
}
