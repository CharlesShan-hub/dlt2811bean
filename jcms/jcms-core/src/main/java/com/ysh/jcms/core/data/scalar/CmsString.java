package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.data.DefaultInnerVisibleString;
import com.ysh.jcms.core.data.core.CmsScalar;

/**
 * <pre>
 * {@code
 * VisibleString ::= VisibleString — 7.1.5
 * }
 * </pre>
 *
 * <p>
 * Generic VisibleString wrapper, backed by DefaultInnerVisibleString. Sync to
 * the real Inner* field is handled by the parent CmsSequence via @Field
 * injection.
 */
public class CmsString extends CmsScalar {

    public CmsString() {
        super(new DefaultInnerVisibleString());
    }
    public CmsString(String v) {
        this();
        value(v);
    }

    public String value() {
        return (String) innerGet();
    }
    public CmsString value(String v) {
        innerSet(v);
        return this;
    }
}
