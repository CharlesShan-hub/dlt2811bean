package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.InnerBoolean;

/**
 * BOOLEAN — 8.2.1.
 *
 * <p>Read/write via {@code innerCache["value"]} only.
 * CmsScalar auto-syncs to/from {@code inner.value} at encode/decode time.
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
