package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerInt16;

/**
 * Wraps {@link InnerInt16} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt16 extends CmsType {

    public CmsInt16() {
        super(new InnerInt16());
    }
    public CmsInt16(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerInt16) inner).value;
    }
    public CmsInt16 value(int v) {
        if (v < Short.MIN_VALUE || v > Short.MAX_VALUE)
            throw new IllegalArgumentException("CmsInt16 out of range [" + Short.MIN_VALUE + "," + Short.MAX_VALUE + "]: " + v);
        ((InnerInt16) inner).value = v;
        return this;
    }
}
