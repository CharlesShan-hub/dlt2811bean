package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerInt8U;

/**
 * Wraps {@link InnerInt8U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt8U extends CmsType {

    public CmsInt8U() {
        super(new InnerInt8U());
    }
    public CmsInt8U(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerInt8U) inner).value & 0xFF;
    }
    public CmsInt8U value(int v) {
        if (v < 0 || v > 0xFF)
            throw new IllegalArgumentException("CmsInt8U out of range [0," + 0xFF + "]: " + v);
        ((InnerInt8U) inner).value = v;
        return this;
    }
}
