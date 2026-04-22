package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT32U type (§7.1.3) — unsigned 32-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT32U   │ 0 .. 4294967295      │ 32   │ long      │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 32-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt32U val = new CmsInt32U(3000000000L);
 * val.set(1000000000L);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt32U val2 = new CmsInt32U().set(3000000000L).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt32U r = new CmsInt32U().decode(pis);
 * long l = r.get();
 * </pre>
 */
public final class CmsInt32U extends AbstractCmsScalar<CmsInt32U> {

    public static final long MIN = 0L;
    public static final long MAX = 4294967295L;

    public CmsInt32U() {
        this(0L);
    }

    public CmsInt32U(long value) {
        super("INT32U", MIN, MAX, value);
    }
}
