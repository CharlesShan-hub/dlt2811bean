package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.InnerObjectReference;

/**
 * ObjectReference ::= VisibleString (SIZE(0..129)) — 7.3.2
 * <p>
 * Wraps {@link InnerObjectReference} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsObjectReference extends CmsScalar {
    public static final int MAX_LEN = 129;

    public CmsObjectReference() {
        super(new InnerObjectReference());
    }
    public CmsObjectReference(String s) {
        this();
        innerSet(s);
    }

    public String value() {
        return (String) innerGet();
    }
    public CmsObjectReference value(String s) {
        if (s != null && s.length() > MAX_LEN)
            throw new IllegalArgumentException("CmsObjectReference too long (max " + MAX_LEN + "): " + s.length());
        innerSet(s);
        return this;
    }
}
