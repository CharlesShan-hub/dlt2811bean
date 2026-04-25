package com.ysh.dlt2811bean.data.numeric;

import com.ysh.dlt2811bean.data.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerInteger;

/**
 * DL/T 2811 INT32 type (§7.1.2) — signed 32-bit integer.
 *
 * <pre>
 * ┌──────────┬────────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                      │ Bits │ Java type │
 * ├──────────┼────────────────────────────┼──────┼───────────┤
 * │ INT32    │ -2147483648 .. 2147483647  │ 32   │ int       │
 * └──────────┴────────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 32-bit signed integer, two's complement form.
 *
 * <pre>
 * // Bean usage
 * CmsInt32 val = new CmsInt32(-1000000);
 * val.set(2000000);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt32 val2 = new CmsInt32().set(-1000000).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt32 r = new CmsInt32().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt32 extends AbstractCmsNumeric<CmsInt32, Integer> {

    public static final int MIN = -(1 << 31); // -2147483648
    public static final int MAX = (1 << 31) - 1; // 2147483647

    public CmsInt32() {
        this(0);
    }

    public CmsInt32(int value) {
        super("INT32", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Integer decodeValue(PerInputStream pis) throws Exception {
        return (int)PerInteger.decode(pis, MIN, MAX);
    }

    private static final CmsInt32 SHARED = new CmsInt32();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt32 read(PerInputStream pis) throws Exception {
        return new CmsInt32().decode(pis);
    }
}
