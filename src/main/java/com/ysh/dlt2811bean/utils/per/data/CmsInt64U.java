package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import java.math.BigInteger;

/**
 * DL/T 2811 INT64U type (§7.1.3) — unsigned 64-bit integer.
 *
 * <pre>
 * ┌──────────┬────────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                      │ Bits │ Java type │
 * ├──────────┼────────────────────────────┼──────┼───────────┤
 * │ INT64U   │ 0 .. 18446744073709551615  │ 64   │ BigInteger│
 * └──────────┴────────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 64-bit integer, 8-byte big-endian byte-aligned encoding.
 *
 * <pre>
 * // Bean usage
 * CmsInt64U val = new CmsInt64U(new BigInteger("12345678901234567890"));
 * val.set(new BigInteger("9876543210987654321"));
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt64U val2 = new CmsInt64U().set(new BigInteger("12345678901234567890")).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt64U r = new CmsInt64U().decode(pis);
 * BigInteger bi = r.get();
 * </pre>
 */
public final class CmsInt64U extends AbstractCmsNumeric<CmsInt64U, BigInteger> {

    public static final BigInteger MIN = BigInteger.ZERO;
    public static final BigInteger MAX = new BigInteger("18446744073709551615");

    public CmsInt64U() {
        this(BigInteger.ZERO);
    }

    public CmsInt64U(BigInteger value) {
        super("INT64U", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        for (byte b : bigIntegerToBytes(get()))
            pos.writeByteAligned(b);
    }

    @Override
    protected BigInteger decodeValue(PerInputStream pis) throws Exception {
        byte[] bytes = new byte[9];
        for (int i = 1; i < bytes.length; i++) {
            bytes[i] = (byte) pis.readByteAligned();
        }
        return new BigInteger(1, bytes);
    }

    private static final CmsInt64U SHARED = new CmsInt64U();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, BigInteger value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt64U read(PerInputStream pis) throws Exception {
        return new CmsInt64U().decode(pis);
    }

    private static byte[] bigIntegerToBytes(BigInteger val) {
        byte[] bytes = val.toByteArray();
        int length = 8;
        int startIndex = (bytes[0] == 0 && bytes.length > length) ? 1 : 0;
        int actualLength = bytes.length - startIndex;

        if (actualLength > length) {
            throw new IllegalArgumentException(
                    String.format("Value too large: requires %d bytes, max is %d", actualLength, length));
        }

        byte[] result = new byte[length];
        int offset = length - actualLength;
        System.arraycopy(bytes, startIndex, result, offset, actualLength);
        return result;
    }
}