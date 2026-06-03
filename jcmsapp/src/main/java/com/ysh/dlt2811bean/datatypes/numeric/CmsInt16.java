package com.ysh.dlt2811bean.datatypes.numeric;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerInteger;

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
public final class CmsInt16 extends AbstractCmsNumeric<CmsInt16, Integer> {

    public static final int MIN = -(1 << 15); // -32768
    public static final int MAX = (1 << 15) - 1; // 32767

    public CmsInt16() {
        this(0);
    }

    public CmsInt16(int value) {
        super("INT16", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Integer decodeValue(PerInputStream pis) throws Exception {
        return (int)PerInteger.decode(pis, MIN, MAX);
    }

    private static final CmsInt16 SHARED = new CmsInt16();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt16 read(PerInputStream pis) throws Exception {
        return new CmsInt16().decode(pis);
    }
}
