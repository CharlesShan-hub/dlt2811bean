package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.math.BigInteger;

public class CmsInt64U extends AbstractCmsScalar<BigInteger> {

    public CmsInt64U() {
        super("INT64U", BigInteger.ZERO);
    }

    public CmsInt64U(BigInteger value) {
        super("INT64U", BigInteger.ZERO);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_Int64U(value.longValue(), buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsInt64U decode(byte[] data) {
        com.sun.jna.ptr.LongByReference v = new com.sun.jna.ptr.LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_Int64U(data, data.length, v);
        long raw = v.getValue();
        BigInteger val;
        if (raw >= 0) {
            val = BigInteger.valueOf(raw);
        } else {
            byte[] bytes = new byte[9];
            bytes[0] = 0;
            for (int i = 0; i < 8; i++) {
                bytes[8 - i] = (byte) ((raw >> (i * 8)) & 0xFF);
            }
            val = new BigInteger(bytes);
        }
        return new CmsInt64U(val);
    }

    @Override
    public CmsInt64U copy() {
        CmsInt64U clone = new CmsInt64U();
        return copyTo(clone);
    }
}
