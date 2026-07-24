package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerObjectReference;

/**
 * ObjectReference ::= VisibleString (SIZE(0..129)) — 7.3.2
 * <p>
 * Wraps {@link InnerObjectReference} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsObjectReference extends CmsType {
    public static final int MAX_LEN = 129;

    public CmsObjectReference() {
        super(new InnerObjectReference());
    }
    public CmsObjectReference(String s) {
        this();
        value(s);
    }

    public String value() {
        return ((InnerObjectReference) inner).value;
    }
    public CmsObjectReference value(String s) {
        if (s != null && s.length() > MAX_LEN)
            throw new IllegalArgumentException("CmsObjectReference too long (max " + MAX_LEN + "): " + s.length());
        ((InnerObjectReference) inner).value = s;
        return this;
    }
}
