package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerBoolean;

/**
 * Wraps {@link InnerBoolean} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsBoolean extends CmsType {

    public CmsBoolean() {
        super(new InnerBoolean());
    }
    public CmsBoolean(boolean value) {
        this();
        ((InnerBoolean) inner).value = value ? 1 : 0;
    }

    public boolean value() {
        return ((InnerBoolean) inner).value != 0;
    }
    public CmsBoolean value(boolean v) {
        ((InnerBoolean) inner).value = v ? 1 : 0;
        return this;
    }
}
