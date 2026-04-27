package com.ysh.dlt2811bean.datatypes.numeric;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerInteger;

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
public final class CmsInt8 extends AbstractCmsNumeric<CmsInt8, Integer> {

    public static final int MIN = -128;
    public static final int MAX = 127;

    public CmsInt8() {
        this(0);
    }

    public CmsInt8(int value) {
        super("INT8", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Integer decodeValue(PerInputStream pis) throws Exception {
        return (int) PerInteger.decode(pis, MIN, MAX);
    }

    private static final CmsInt8 SHARED = new CmsInt8();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt8 read(PerInputStream pis) throws Exception {
        return new CmsInt8().decode(pis);
    }
}
