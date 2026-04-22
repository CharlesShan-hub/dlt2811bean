package com.ysh.dlt2811bean.utils.per.data;

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
public final class CmsInt64U extends AbstractCmsScalar<CmsInt64U> {

    public static final BigInteger MIN = BigInteger.ZERO;
    public static final BigInteger MAX = new BigInteger("18446744073709551615");

    public CmsInt64U() {
        this(BigInteger.ZERO);
    }

    public CmsInt64U(BigInteger value) {
        super("INT64U", MIN, MAX, value);
    }
}