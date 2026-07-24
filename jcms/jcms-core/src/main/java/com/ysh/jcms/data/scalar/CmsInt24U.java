package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerInt24U;

/**
 * Wraps {@link InnerInt24U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt24U extends CmsType {

    public static final int MAX = 16777215;

    public CmsInt24U() {
        super(new InnerInt24U());
    }
    public CmsInt24U(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerInt24U) inner).value;
    }
    public CmsInt24U value(int v) {
        if (v < 0 || v > MAX)
            throw new IllegalArgumentException("CmsInt24U out of range [0," + MAX + "]: " + v);
        ((InnerInt24U) inner).value = v;
        return this;
    }
}
