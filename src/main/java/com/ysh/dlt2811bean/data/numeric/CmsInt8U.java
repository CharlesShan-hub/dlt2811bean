package com.ysh.dlt2811bean.data.numeric;

import com.ysh.dlt2811bean.data.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerInteger;

/**
 * DL/T 2811 INT8U type (§7.1.3) — unsigned 8-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────┬──────┬───────────┐
 * │ 2811     │ Range            │ Bits │ Java type │
 * ├──────────┼──────────────────┼──────┼───────────┤
 * │ INT8U    │ 0 .. 255         │ 8    │ int       │
 * └──────────┴──────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: unsigned 8-bit integer.
 *
 * <pre>
 * // Bean usage
 * CmsInt8U val = new CmsInt8U(200);
 * val.set(100);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt8U val2 = new CmsInt8U().set(200).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt8U r = new CmsInt8U().decode(pis);
 * int i = r.get();
 *
 * // Static usage
 * CmsInt8U.write(pos, 200);
 * CmsInt8U decoded = CmsInt8U.read(pis);
 * </pre>
 */
public final class CmsInt8U extends AbstractCmsNumeric<CmsInt8U, Integer> {

    public static final int MIN = 0;
    public static final int MAX = (1 << 8) - 1; // 255

    public CmsInt8U() {
        this(0);
    }

    public CmsInt8U(int value) {
        super("INT8U", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get(), MIN, MAX);
    }

    @Override
    protected Integer decodeValue(PerInputStream pis) throws Exception {
        return (int)PerInteger.decode(pis, MIN, MAX);
    }

    private static final CmsInt8U SHARED = new CmsInt8U();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt8U read(PerInputStream pis) throws Exception {
        return new CmsInt8U().decode(pis);
    }
}
