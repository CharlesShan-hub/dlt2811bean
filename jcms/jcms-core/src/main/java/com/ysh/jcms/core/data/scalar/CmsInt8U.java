package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerInt8U;

/**
 * <pre>
 * {@code
 * Int8U ::= INTEGER (0..255) — 7.1.2
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerInt8U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt8U extends CmsScalar {

    public CmsInt8U() {
        super(new InnerInt8U());
    }
    public CmsInt8U(int value) {
        this();
        value(value);
    }

    public int value() {
        Integer v = (Integer) innerGet();
        return v != null ? v & 0xFF : 0;
    }
    public CmsInt8U value(int v) {
        if (v < 0 || v > 0xFF)
            throw new IllegalArgumentException("CmsInt8U out of range [0," + 0xFF + "]: " + v);
        innerSet(v);
        return this;
    }
}
