package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import java.math.BigInteger;

/**
 * typedef struct { uint64_t value; } cms_int64u_t;
 * sizeof = 8
 * Java long 不足以覆盖 0..2^64-1，用 BigInteger。
 */
public class CmsInt64U extends CmsType {

    private BigInteger value = BigInteger.ZERO;

    public CmsInt64U() {}
    public CmsInt64U(BigInteger value) { this.value = value.and(MAX); write(); }

    public static final BigInteger MAX = new BigInteger("18446744073709551615");

    public BigInteger value() { return value; }
    public CmsInt64U value(BigInteger v) { this.value = v.and(MAX); write(); return this; }

    @Override protected int calcNativeSize() { return 8; }

    @Override
    public void write() {
        /* write as 8 bytes big-endian; don't mutate this.value */
        BigInteger tmp = value;
        for (int i = 7; i >= 0; i--) {
            nativePtr.setByte(i, (byte) (tmp.longValue() & 0xFF));
            tmp = tmp.shiftRight(8);
        }
    }

    @Override
    public void read() {
        byte[] b = new byte[8];
        for (int i = 0; i < 8; i++) b[i] = nativePtr.getByte(i);
        this.value = new BigInteger(1, b);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeInt64U(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeInt64U(nativePtr, data); read(); }
}
