package com.ysh.dlt2811bean.data.numeric;

import com.ysh.dlt2811bean.data.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerReal;

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
public final class CmsFloat64 extends AbstractCmsNumeric<CmsFloat64, Double> {

    public CmsFloat64() {
        this(0.0);
    }

    public CmsFloat64(double value) {
        super("FLOAT64", value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerReal.encodeFloat64(pos, get());
    }

    @Override
    protected Double decodeValue(PerInputStream pis) throws Exception {
        return PerReal.decodeFloat64(pis);
    }

    private static final CmsFloat64 SHARED = new CmsFloat64();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, double value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsFloat64 read(PerInputStream pis) throws Exception {
        return new CmsFloat64().decode(pis);
    }
}
