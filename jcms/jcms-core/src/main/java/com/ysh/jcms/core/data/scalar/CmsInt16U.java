package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerInt16U;

/**
 * <pre>
 * {@code
 * Int16U ::= INTEGER (0..65535) — 7.1.2
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerInt16U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt16U extends CmsScalar {

    public CmsInt16U() {
        super(new InnerInt16U());
    }
    public CmsInt16U(int value) {
        this();
        value(value);
    }

    public int value() {
        Integer v = (Integer) innerGet();
        return v != null ? v & 0xFFFF : 0;
    }
    public CmsInt16U value(int v) {
        if (v < 0 || v > 0xFFFF)
            throw new IllegalArgumentException("CmsInt16U out of range [0," + 0xFFFF + "]: " + v);
        innerSet(v);
        return this;
    }
}
