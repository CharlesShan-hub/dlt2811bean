package com.ysh.dlt2811bean.datatypes.numeric;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerInteger;

/**
 * DL/T 2811 INT32U type (§7.1.3) — unsigned 32-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT32U   │ 0 .. 4294967295      │ 32   │ long      │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 32-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt32U val = new CmsInt32U(3000000000L);
 * val.set(1000000000L);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt32U val2 = new CmsInt32U().set(3000000000L).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt32U r = new CmsInt32U().decode(pis);
 * long l = r.get();
 * </pre>
 */
public final class CmsInt32U extends AbstractCmsNumeric<CmsInt32U, Long> {

    public static final long MIN = 0L;
    public static final long MAX = 4294967295L;

    public CmsInt32U() {
        this(0L);
    }

    public CmsInt32U(long value) {
        super("INT32U", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Long decodeValue(PerInputStream pis) throws Exception {
        return PerInteger.decode(pis, MIN, MAX);
    }

    private static final CmsInt32U SHARED = new CmsInt32U();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt32U read(PerInputStream pis) throws Exception {
        return new CmsInt32U().decode(pis);
    }
}
