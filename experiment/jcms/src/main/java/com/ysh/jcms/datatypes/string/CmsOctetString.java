package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsOctetString extends AbstractCmsScalar<CmsOctetString, byte[]> {

    public CmsOctetString() {
        super("OctetString", new byte[0]);
    }

    public CmsOctetString(byte[] value) {
        super("OctetString", new byte[0]);
        set(value);
    }

    @Override
    protected int encodeBufSize() {
        return 65536;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_octet_string_encode(value, value.length, 65535, buf, outLen);
    }

    public static CmsOctetString decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_octet_string_decode(data, data.length, 65535, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsOctetString(result);
    }
}
