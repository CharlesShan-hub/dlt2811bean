package com.ysh.dlt2811bean.utils.per.data;

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
public final class CmsBoolean extends AbstractCmsScalar<CmsBoolean> {

    public CmsBoolean(boolean value) {
        super("BOOLEAN", 0, 1, value);
    }

    public CmsBoolean() {
        this(false);
    }
}
