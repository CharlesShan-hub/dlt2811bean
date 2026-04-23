package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;

/**
 * DL/T 2811 INT16U type (§7.1.3) — unsigned 16-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT16U   │ 0 .. 65535           │ 16   │ int       │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 16-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt16U val = new CmsInt16U(50000);
 * val.set(10000);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt16U val2 = new CmsInt16U().set(50000).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt16U r = new CmsInt16U().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt16U extends AbstractCmsNumeric<CmsInt16U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = (1 << 16) - 1; // 65535

    public CmsInt16U() {
        this(0);
    }

    public CmsInt16U(int value) {
        super("INT16U", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Integer decodeValue(PerInputStream pis) throws Exception {
        return (int)PerInteger.decode(pis, MIN, MAX);
    }

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        new CmsInt16U(value).encode(pos);
    }

    /** Static write with instance (null encodes default 0). */
    public static void write(PerOutputStream pos, CmsInt16U obj) {
        new CmsInt16U(obj == null ? 0 : obj.get()).encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt16U read(PerInputStream pis) throws Exception {
        return new CmsInt16U().decode(pis);
    }
}
