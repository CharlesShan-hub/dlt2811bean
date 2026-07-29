package com.ysh.jcms.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerBoolean;

/**
 * BOOLEAN — 8.2.1.
 *
 * <p>Read/write via CmsScalar which reads {@code inner.value} directly.
 */
public class CmsBoolean extends CmsScalar {

    public CmsBoolean() {
        super(new InnerBoolean());
    }
    public CmsBoolean(boolean value) {
        this();
        innerSet(value ? 1 : 0);
    }

    public boolean value() {
        Integer v = (Integer) innerGet();
        return v != null && v != 0;
    }
    public CmsBoolean value(boolean v) {
        innerSet(v ? 1 : 0);
        return this;
    }
}
