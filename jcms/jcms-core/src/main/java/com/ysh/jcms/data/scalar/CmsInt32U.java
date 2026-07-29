package com.ysh.jcms.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerInt32U;

/**
 * Wraps {@link InnerInt32U} for PER encode/decode via Rust (libasn1.so).
 * <p>
 * Int32U ::= INTEGER (0..4294967295) — 32-bit unsigned.
 */
public class CmsInt32U extends CmsScalar {

    public static final long MAX_VALUE = 0xFFFFFFFFL;

    public CmsInt32U() {
        super(new InnerInt32U());
    }
    public CmsInt32U(long value) {
        this();
        value(value);
    }

    /** Get unsigned int value as long (always 0..4294967295). */
    public long value() {
        Integer v = (Integer) innerGet();
        return v != null ? (v & 0xFFFFFFFFL) : 0L;
    }
    public CmsInt32U value(long v) {
        if (v < 0 || v > MAX_VALUE)
            throw new IllegalArgumentException("CmsInt32U out of range [0," + MAX_VALUE + "]: " + v);
        innerSet((int) v);
        return this;
    }
}
