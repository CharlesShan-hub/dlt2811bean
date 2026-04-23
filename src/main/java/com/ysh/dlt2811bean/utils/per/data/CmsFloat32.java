package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerReal;

/**
 * DL/T 2811 FLOAT32 type — IEEE 754 single-precision floating-point (§7.1.4).
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
 * val.set(2.718f);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsFloat32 val2 = new CmsFloat32().set(3.14f).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsFloat32 r = new CmsFloat32().decode(pis);
 * float f = r.get();
 * </pre>
 */
public final class CmsFloat32 extends AbstractCmsNumeric<CmsFloat32, Float> {

    public CmsFloat32() {
        this(0.0f);
    }

    public CmsFloat32(float value) {
        super("FLOAT32", value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerReal.encodeFloat32(pos, get());
    }

    @Override
    protected Float decodeValue(PerInputStream pis) throws Exception {
        return PerReal.decodeFloat32(pis);
    }

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, float value) {
        new CmsFloat32(value).encode(pos);
    }

    /** Static write with instance (null encodes default 0). */
    public static void write(PerOutputStream pos, CmsFloat32 obj) {
        new CmsFloat32(obj == null ? 0.0f : obj.get()).encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsFloat32 read(PerInputStream pis) throws Exception {
        return new CmsFloat32().decode(pis);
    }
}
