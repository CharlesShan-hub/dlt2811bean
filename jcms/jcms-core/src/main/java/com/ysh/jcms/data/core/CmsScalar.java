package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.V;

/**
 * Base class for scalar types whose Inner* stores a single value in {@code _v}.
 *
 * <p>Reads/writes go straight to {@code inner._v["_"]}, so the auto-generated
 * Lombok getters/setters on Inner* (e.g. the {@code value} field of
 * DefaultInner*) are never involved.
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
        // Read directly from _v — the DefaultInner* subclasses have Lombok-generated
        // getValue() (e.g. returning the `value` field), which would bypass _v.
        return V.getVal(inner._v);
    }

    protected void innerSet(Object v) {
        V.setVal(inner._v, v);
    }
}
