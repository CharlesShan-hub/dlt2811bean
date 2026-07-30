package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;

/**
 * Base class for scalar types whose Inner* has a public {@code value} field.
 *
 * <p>Reads via {@code inner.getValue()} and writes via {@code inner.setValue()},
 * both provided by the auto-generated Inner* classes. No reflection needed.
 *
 * <p>The no-arg constructor creates a placeholder backed by {@link InnerEmpty}.
 */
public abstract class CmsScalar extends CmsType {

    private Object directValue;

    protected CmsScalar() {
        this(new InnerEmpty());
    }

    protected CmsScalar(InnerBase inner) {
        super(inner);
        this.directValue = inner.getValue();
    }

    protected Object innerGet() {
        Object v = inner.getValue();
        if (v != null) return v;
        return directValue;
    }

    protected void innerSet(Object v) {
        this.directValue = v;
        inner.setValue(v);
    }

    @Override
    public void syncFromInner() {
        Object v = inner.getValue();
        if (v != null) {
            this.directValue = v;
        }
    }

    @Override
    public void syncToInner() {
        super.syncToInner();
    }
}