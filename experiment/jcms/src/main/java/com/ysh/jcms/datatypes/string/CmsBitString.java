package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsBitString extends AbstractCmsScalar<byte[]> {

    public CmsBitString() {
        super("BitString", new byte[0]);
    }

    public CmsBitString(byte[] value) {
        super("BitString", new byte[0]);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_bit_string_encode(value, value.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBitString decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_bit_string_decode(data, data.length, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsBitString(result);
    }

    @Override
    public CmsBitString copy() {
        CmsBitString clone = new CmsBitString();
        return copyTo(clone);
    }
}
