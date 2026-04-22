package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerReal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 FLOAT64 type — IEEE 754 double-precision floating-point.
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
 * CmsFloat64.encode(pos, val);
 *
 * // Quick usage — pass raw double directly
 * CmsFloat64.encode(pos, 220.5);
 *
 * // Decode always returns a bean
 * CmsFloat64 r = CmsFloat64.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsFloat64 {

    private double value;

    public CmsFloat64() {
        this.value = 0.0;
    }

    public CmsFloat64(double value) {
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsFloat64 bean. */
    public static void encode(PerOutputStream pos, CmsFloat64 val) {
        PerReal.encodeFloat64(pos, val.value);
    }

    /** Encodes a raw double value. */
    public static void encode(PerOutputStream pos, double val) {
        PerReal.encodeFloat64(pos, val);
    }

    public static CmsFloat64 decode(PerInputStream pis) throws PerDecodeException {
        return new CmsFloat64(PerReal.decodeFloat64(pis));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
