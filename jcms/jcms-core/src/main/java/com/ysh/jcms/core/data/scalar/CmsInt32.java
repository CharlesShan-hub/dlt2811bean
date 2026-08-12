package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.core.data.core.CmsScalar;
import com.ysh.jcms.data.InnerInt32;

/**
 * <pre>
 * {@code
 * Int32 ::= INTEGER (-2147483648..2147483647) — 7.1.2
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerInt32} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt32 extends CmsScalar {

    public CmsInt32() {
        super(new InnerInt32());
    }
    public CmsInt32(int value) {
        this();
        value(value);
    }

    public int value() {
        Integer v = (Integer) innerGet();
        return v != null ? v : 0;
    }
    public CmsInt32 value(int v) {
        if (v < Integer.MIN_VALUE || v > Integer.MAX_VALUE)
            throw new IllegalArgumentException("CmsInt32 out of range [" + Integer.MIN_VALUE + "," + Integer.MAX_VALUE + "]: " + v);
        innerSet(v);
        return this;
    }
}
