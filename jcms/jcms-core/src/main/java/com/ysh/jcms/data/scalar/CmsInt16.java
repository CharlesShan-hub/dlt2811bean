package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.InnerInt16;

/**
 * Wraps {@link InnerInt16} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt16 extends CmsScalar {

    public CmsInt16() {
        super(new InnerInt16());
    }
    public CmsInt16(int value) {
        this();
        value(value);
    }

    public int value() {
        Integer v = (Integer) innerGet();
        return v != null ? v : 0;
    }
    public CmsInt16 value(int v) {
        if (v < Short.MIN_VALUE || v > Short.MAX_VALUE)
            throw new IllegalArgumentException("CmsInt16 out of range [" + Short.MIN_VALUE + "," + Short.MAX_VALUE + "]: " + v);
        innerSet(v);
        return this;
    }
}
