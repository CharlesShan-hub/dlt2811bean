package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerReal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 FLOAT32 type — IEEE 754 single-precision floating-point.
 *
 * <pre>
 * ┌──────────┬────────────────────┬─────────────────┬───────────┐
 * │ 2811     │ Range              │ Bits            │ Java type │
 * ├──────────┼────────────────────┼─────────────────┼───────────┤
 * │ FLOAT32  │ IEEE 754 binary32  │ 1 (zero) / 40   │ float     │
 * └──────────┴────────────────────┴─────────────────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 1-bit flag (0 = zero value, 1 = IEEE 754 bytes follow after alignment).
 *
 * <pre>
 * // Bean usage
 * CmsFloat32 val = new CmsFloat32(3.14f);
 * CmsFloat32.encode(pos, val);
 *
 * // Quick usage — pass raw float directly
 * CmsFloat32.encode(pos, 3.14f);
 *
 * // Decode always returns a bean
 * CmsFloat32 r = CmsFloat32.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsFloat32 {

    private float value;

    public CmsFloat32() {
        this.value = 0.0f;
    }

    public CmsFloat32(float value) {
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsFloat32 bean. */
    public static void encode(PerOutputStream pos, CmsFloat32 val) {
        PerReal.encodeFloat32(pos, val.value);
    }

    /** Encodes a raw float value. */
    public static void encode(PerOutputStream pos, float val) {
        PerReal.encodeFloat32(pos, val);
    }

    public static CmsFloat32 decode(PerInputStream pis) throws PerDecodeException {
        return new CmsFloat32(PerReal.decodeFloat32(pis));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
