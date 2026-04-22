package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT64 type (§7.1.2) — signed 64-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                                        │ Bits │ Java type │
 * ├──────────┼──────────────────────────────────────────────┼──────┼───────────┤
 * │ INT64    │ -9223372036854775808 .. 9223372036854775807  │ 64   │ long      │
 * └──────────┴──────────────────────────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 64-bit signed integer, two's complement form, 8-byte big-endian byte-aligned encoding.
 *
 * <pre>
 * // Bean usage
 * CmsInt64 val = new CmsInt64(-1000000000000L);
 * val.set(2000000000000L);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt64 val2 = new CmsInt64().set(-1000000000000L).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt64 r = new CmsInt64().decode(pis);
 * long l = r.get();
 * </pre>
 */
public final class CmsInt64 extends AbstractCmsScalar<CmsInt64> {

    public static final long MIN = Long.MIN_VALUE;
    public static final long MAX = Long.MAX_VALUE;

    public CmsInt64() {
        this(0L);
    }

    public CmsInt64(long value) {
        super("INT64", MIN, MAX, value);
    }
}
