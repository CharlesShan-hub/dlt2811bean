package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import java.math.BigInteger;

public class CmsInt64U extends AbstractCmsNumeric<CmsInt64U, BigInteger> {

    public CmsInt64U() {
        this(BigInteger.ZERO);
    }

    public CmsInt64U(BigInteger value) {
        super("INT64U", value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_int64u_encode(value.longValue(), buf, outLen);
    }

    public static CmsInt64U decode(byte[] data) {
        com.sun.jna.ptr.LongByReference v = new com.sun.jna.ptr.LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_int64u_decode(data, data.length, v);
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
}
