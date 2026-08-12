package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.core.data.core.CmsScalar;
import com.ysh.jcms.data.InnerInt8;

/**
 * <pre>
 * {@code
 * Int8 ::= INTEGER (-128..127) — 7.1.2
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerInt8} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt8 extends CmsScalar {

    public CmsInt8() {
        super(new InnerInt8());
    }
    public CmsInt8(int value) {
        this();
        value(value);
    }

    public int value() {
        Integer v = (Integer) innerGet();
        return v != null ? v : 0;
    }
    public CmsInt8 value(int v) {
        if (v < Byte.MIN_VALUE || v > Byte.MAX_VALUE)
            throw new IllegalArgumentException("CmsInt8 out of range [" + Byte.MIN_VALUE + "," + Byte.MAX_VALUE + "]: " + v);
        innerSet(v);
        return this;
    }
}
