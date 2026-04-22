package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT16U type (§7.1.3) — unsigned 16-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT16U   │ 0 .. 65535           │ 16   │ int       │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 16-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt16U val = new CmsInt16U(50000);
 * val.set(10000);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt16U val2 = new CmsInt16U().set(50000).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt16U r = new CmsInt16U().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt16U extends AbstractCmsScalar<CmsInt16U> {

    public static final int MIN = 0;
    public static final int MAX = 65535;

    public CmsInt16U() {
        this(0);
    }

    public CmsInt16U(int value) {
        super("INT16U", MIN, MAX, value);
    }
}
