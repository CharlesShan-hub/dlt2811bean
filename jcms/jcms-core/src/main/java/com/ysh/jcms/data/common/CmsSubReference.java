package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerSubReference;

/**
 * SubReference ::= VisibleString (SIZE(0..129)) — 7.3.3
 * <p>
 * Wraps {@link InnerSubReference} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsSubReference extends CmsType {
    public static final int MAX_LEN = 129;

    public CmsSubReference() {
        super(new InnerSubReference());
    }
    public CmsSubReference(String s) {
        this();
        value(s);
    }

    public String value() {
        return ((InnerSubReference) inner).value;
    }
    public CmsSubReference value(String s) {
        if (s != null && s.length() > MAX_LEN)
            throw new IllegalArgumentException("CmsSubReference too long (max " + MAX_LEN + "): " + s.length());
        ((InnerSubReference) inner).value = s;
        return this;
    }
}
