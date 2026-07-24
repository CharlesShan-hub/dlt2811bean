package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerInt16U;

/**
 * Wraps {@link InnerInt16U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt16U extends CmsType {

    public CmsInt16U() {
        super(new InnerInt16U());
    }
    public CmsInt16U(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerInt16U) inner).value & 0xFFFF;
    }
    public CmsInt16U value(int v) {
        if (v < 0 || v > 0xFFFF)
            throw new IllegalArgumentException("CmsInt16U out of range [0," + 0xFFFF + "]: " + v);
        ((InnerInt16U) inner).value = v;
        return this;
    }
}
