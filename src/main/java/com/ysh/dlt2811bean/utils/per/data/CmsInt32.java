package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT32 type (§7.1.2) — signed 32-bit integer.
 *
 * <pre>
 * ┌──────────┬────────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                      │ Bits │ Java type │
 * ├──────────┼────────────────────────────┼──────┼───────────┤
 * │ INT32    │ -2147483648 .. 2147483647  │ 32   │ int       │
 * └──────────┴────────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 32-bit signed integer, two's complement form.
 *
 * <pre>
 * // Bean usage
 * CmsInt32 val = new CmsInt32(-1000000);
 * val.set(2000000);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt32 val2 = new CmsInt32().set(-1000000).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt32 r = new CmsInt32().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt32 extends AbstractCmsScalar<CmsInt32> {

    public static final int MIN = -2147483648;
    public static final int MAX = 2147483647;

    public CmsInt32() {
        this(0);
    }

    public CmsInt32(int value) {
        super("INT32", MIN, MAX, value);
    }
}
