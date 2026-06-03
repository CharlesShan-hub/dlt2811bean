package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;
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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_int64u_encode(value.longValue(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encodeUnconstrained(pos, value.longValue());
    }

    private static BigInteger unsignedLongToBigInteger(long raw) {
        if (raw >= 0) return BigInteger.valueOf(raw);
        byte[] bytes = new byte[9];
        bytes[0] = 0;
        for (int i = 0; i < 8; i++) {
            bytes[8 - i] = (byte) ((raw >> (i * 8)) & 0xFF);
        }
        return new BigInteger(bytes);
    }

    public static CmsInt64U decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            com.sun.jna.ptr.LongByReference v = new com.sun.jna.ptr.LongByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_int64u_decode(data, data.length, v);
            return new CmsInt64U(unsignedLongToBigInteger(v.getValue()));
        }
        long raw = PerInteger.decodeUnconstrained(new PerInputStream(data));
        return new CmsInt64U(unsignedLongToBigInteger(raw));
    }
}
