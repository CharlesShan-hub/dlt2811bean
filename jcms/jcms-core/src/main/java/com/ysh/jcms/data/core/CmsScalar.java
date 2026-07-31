package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;

/**
 * Base class for scalar types whose Inner* stores a single value in {@code _v}.
 *
 * <p>Reads via {@code inner.getValue()} and writes via {@code inner.setValue()},
 * both provided by the auto-generated Inner* classes. No reflection needed.
 *
 * <p>The no-arg constructor creates a placeholder backed by {@link InnerEmpty}.
 */
public abstract class CmsScalar extends CmsType {

    protected CmsScalar() {
        this(new InnerEmpty());
    }

    protected CmsScalar(InnerBase inner) {
        super(inner);
    }

    protected Object innerGet() {
        return inner.getValue();
    }

    protected void innerSet(Object v) {
        inner.setValue(v);
    }
}
