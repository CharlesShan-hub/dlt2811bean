package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsOctetString extends AbstractCmsScalar<byte[]> {

    public CmsOctetString() {
        super("OctetString", new byte[0]);
    }

    public CmsOctetString(byte[] value) {
        super("OctetString", new byte[0]);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_OctetString(value, value.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsOctetString decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFI.INSTANCE.cms_decode_OctetString(data, data.length, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsOctetString(result);
    }

    @Override
    public CmsOctetString copy() {
        CmsOctetString clone = new CmsOctetString();
        return copyTo(clone);
    }
}
