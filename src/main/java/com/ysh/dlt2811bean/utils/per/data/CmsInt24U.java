package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;

/**
 * DL/T 2811 INT24U type — unsigned 24-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT24U   │ 0 .. 16777215        │ 24   │ int       │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 24-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt24U val = new CmsInt24U(1234567);
 * val.set(1000000);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt24U val2 = new CmsInt24U().set(1234567).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt24U r = new CmsInt24U().decode(pis);
 * int i = r.get();
 * </pre>
 */
public final class CmsInt24U extends AbstractCmsNumeric<CmsInt24U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = (1 << 24) - 1; // 16777215

    public CmsInt24U() {
        this(0);
    }

    public CmsInt24U(int value) {
        super("INT24U", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Integer decodeValue(PerInputStream pis) throws Exception {
        return (int) PerInteger.decode(pis, MIN, MAX);
    }

    private static final CmsInt24U SHARED = new CmsInt24U();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt24U read(PerInputStream pis) throws Exception {
        return new CmsInt24U().decode(pis);
    }
}