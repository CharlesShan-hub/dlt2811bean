package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT16 type (§7.1.2) — signed 16-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT16    │ -32768 .. 32767      │ 16   │ int       │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 16-bit signed integer, two's complement form.
 *
 * <pre>
 * // Bean usage
 * CmsInt16 val = new CmsInt16(1000);
 * val.set(-500);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt16 val2 = new CmsInt16().set(1000).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt16 r = new CmsInt16().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt16 extends AbstractCmsScalar<CmsInt16> {

    public static final int MIN = -32768;
    public static final int MAX = 32767;

    public CmsInt16() {
        this(0);
    }

    public CmsInt16(int value) {
        super("INT16", MIN, MAX, value);
    }
}
