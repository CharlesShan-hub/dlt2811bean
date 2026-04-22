package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT8 type (§7.1.2) — signed 8-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────┬──────┬───────────┐
 * │ 2811     │ Range            │ Bits │ Java type │
 * ├──────────┼──────────────────┼──────┼───────────┤
 * │ INT8     │ -128 .. 127      │ 8    │ int       │
 * └──────────┴──────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 8-bit signed integer, two's complement form.
 *
 * <pre>
 * // Bean usage
 * CmsInt8 val = new CmsInt8(42);
 * val.set(100);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt8 val2 = new CmsInt8().set(42).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt8 r = new CmsInt8().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt8 extends AbstractCmsScalar<CmsInt8> {

    public static final int MIN = -128;
    public static final int MAX = 127;

    public CmsInt8() {
        this(0);
    }

    public CmsInt8(int value) {
        super("INT8", MIN, MAX, value);
    }
}
