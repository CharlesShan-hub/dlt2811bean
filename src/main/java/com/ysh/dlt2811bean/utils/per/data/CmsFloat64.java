package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 FLOAT64 type — IEEE 754 double-precision floating-point (§7.1.4).
 *
 * <pre>
 * ┌──────────┬────────────────────┬─────────────────┬───────────┐
 * │ 2811     │ Range              │ Bits            │ Java type │
 * ├──────────┼────────────────────┼─────────────────┼───────────┤
 * │ FLOAT64  │ IEEE 754 binary64  │ 1 (zero) / 72   │ double    │
 * └──────────┴────────────────────┴─────────────────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 1-bit flag (0 = zero value, 1 = IEEE 754 bytes follow after alignment).
 *
 * <pre>
 * // Bean usage
 * CmsFloat64 val = new CmsFloat64(220.5);
 * val.set(110.0);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsFloat64 val2 = new CmsFloat64().set(220.5).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsFloat64 r = new CmsFloat64().decode(pis);
 * double d = r.get();
 * </pre>
 */
public final class CmsFloat64 extends AbstractCmsScalar<CmsFloat64> {

    public CmsFloat64() {
        this(0.0);
    }

    public CmsFloat64(double value) {
        super("FLOAT64", value);
    }
}
