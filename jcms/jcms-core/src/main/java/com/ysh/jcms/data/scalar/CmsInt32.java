package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerInt32;

/**
 * Wraps {@link InnerInt32} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt32 extends CmsType {

    public CmsInt32() {
        super(new InnerInt32());
    }
    public CmsInt32(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerInt32) inner).value;
    }
    public CmsInt32 value(int v) {
        if (v < Integer.MIN_VALUE || v > Integer.MAX_VALUE)
            throw new IllegalArgumentException("CmsInt32 out of range [" + Integer.MIN_VALUE + "," + Integer.MAX_VALUE + "]: " + v);
        ((InnerInt32) inner).value = v;
        return this;
    }
}
