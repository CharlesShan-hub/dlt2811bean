package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;

/**
 * DL/T 2811 boolean type (§7.1.1).
 *
 * <pre>
 * ┌──────────┬─────────────────┬──────┬───────────┐
 * │ 2811     │ Range           │ Bits │ Java type │
 * ├──────────┼─────────────────┼──────┼───────────┤
 * │ BOOLEAN  │ FALSE | TRUE    │ 1    │ boolean   │
 * └──────────┴─────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: single bit, FALSE=0, TRUE=1.
 *
 * <pre>
 * // Bean usage
 * CmsBoolean val = new CmsBoolean(true);
 * val.set(false);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsBoolean val2 = new CmsBoolean().set(true).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsBoolean r = new CmsBoolean().decode(pis);
 * boolean b = r.get();
 * </pre>
 */
public final class CmsBoolean extends AbstractCmsScalar<CmsBoolean, Boolean> {

    public CmsBoolean(boolean value) {
        super("BOOLEAN", 0, 1, value);
    }

    public CmsBoolean() {
        this(false);
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerInteger.encode(pos, get() ? 1L : 0L, 0L, 1L);
    }

    @Override
    public CmsBoolean decode(PerInputStream pis) throws Exception {
        set(PerInteger.decode(pis, 0L, 1L) == 1L);
        return this;
    }

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, boolean value) {
        new CmsBoolean(value).encode(pos);
    }

    /** Static write with instance (null encodes default false). */
    public static void write(PerOutputStream pos, CmsBoolean obj) {
        new CmsBoolean(obj != null && obj.get()).encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsBoolean read(PerInputStream pis) throws Exception {
        return new CmsBoolean().decode(pis);
    }
}
