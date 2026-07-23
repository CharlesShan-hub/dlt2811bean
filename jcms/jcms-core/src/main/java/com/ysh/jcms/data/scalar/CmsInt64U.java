package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt64U;
import com.ysh.jcms.data.InnerNative;
import java.math.BigInteger;

/**
 * Wraps {@link InnerInt64U} for PER encode/decode via Rust (libasn1.so).
 * InnerInt64U stores the value as a signed long; CmsInt64U exposes BigInteger
 * for the full unsigned 64-bit range.
 */
public class CmsInt64U extends CmsType {

    public static final BigInteger MAX = new BigInteger("18446744073709551615");

    private transient InnerInt64U inner = new InnerInt64U();

    public CmsInt64U() {
        super(Codec.INT64U);
    }
    public CmsInt64U(BigInteger value) {
        super(Codec.INT64U);
        inner.value = bigIntToLong(value.and(MAX));
    }

    public BigInteger value() {
        return longToBigInt(inner.value);
    }
    public CmsInt64U value(BigInteger v) {
        inner.value = bigIntToLong(v.and(MAX));
        return this;
    }

    /** Convert unsigned BigInteger (0..2^64-1) to signed long bit pattern. */
    private static long bigIntToLong(BigInteger v) {
        return v.longValue(); // truncates to 64 bits — correct for unsigned
    }
    /** Convert signed long bit pattern to unsigned BigInteger (0..2^64-1). */
    private static BigInteger longToBigInt(long v) {
        if (v >= 0) return BigInteger.valueOf(v);
        byte[] buf = new byte[8];
        long tmp = v;
        for (int i = 7; i >= 0; i--) {
            buf[i] = (byte) tmp;
            tmp >>>= 8;
        }
        return new BigInteger(1, buf);
    }

    @Override
    public byte[] encode() {
        // Send unsigned BigInteger string; inner.value is signed long
        return InnerNative.encode("Int64U", "aper", value().toString());
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt64U.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 8;
    }
    @Override
    public void write() {
        BigInteger tmp = value();
        for (int i = 7; i >= 0; i--) {
            nativePtr.setByte(i, (byte) (tmp.longValue() & 0xFF));
            tmp = tmp.shiftRight(8);
        }
    }
    @Override
    public void read() {
        byte[] b = new byte[8];
        for (int i = 0; i < 8; i++)
            b[i] = nativePtr.getByte(i);
        inner.value = bigIntToLong(new BigInteger(1, b));
    }
}
