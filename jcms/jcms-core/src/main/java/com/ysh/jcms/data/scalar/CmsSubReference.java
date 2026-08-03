package com.ysh.jcms.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerSubReference;

/**
 * <pre>
 * {@code
 * SubReference ::= VisibleString (SIZE(0..129)) — 7.3.3
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerSubReference} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsSubReference extends CmsScalar {
    public static final int MAX_LEN = 129;

    public CmsSubReference() {
        super(new InnerSubReference());
    }
    public CmsSubReference(String s) {
        this();
        innerSet(s);
    }

    public String value() {
        return (String) innerGet();
    }
    public CmsSubReference value(String s) {
        if (s != null && s.length() > MAX_LEN)
            throw new IllegalArgumentException("CmsSubReference too long (max " + MAX_LEN + "): " + s.length());
        innerSet(s);
        return this;
    }
}
