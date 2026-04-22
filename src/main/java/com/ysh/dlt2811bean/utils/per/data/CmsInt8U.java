package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 INT8U type (§7.1.3) — unsigned 8-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────┬──────┬───────────┐
 * │ 2811     │ Range            │ Bits │ Java type │
 * ├──────────┼──────────────────┼──────┼───────────┤
 * │ INT8U    │ 0 .. 255         │ 8    │ int       │
 * └──────────┴──────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 8-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt8U val = new CmsInt8U(200);
 * val.set(100);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt8U val2 = new CmsInt8U().set(200).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt8U r = new CmsInt8U().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt8U extends AbstractCmsScalar<CmsInt8U> {

    public static final int MIN = 0;
    public static final int MAX = 255;

    public CmsInt8U() {
        this(0);
    }

    public CmsInt8U(int value) {
        super("INT8U", MIN, MAX, value);
    }
}
