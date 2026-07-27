package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.InnerObjectName;

/**
 * ObjectName ::= VisibleString (SIZE(0..64)) — 7.3.1
 * <p>
 * Wraps {@link InnerObjectName} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsObjectName extends CmsScalar {
    public static final int MAX_LEN = 64;

    public CmsObjectName() {
        super(new InnerObjectName());
    }
    public CmsObjectName(String s) {
        this();
        innerSet(s);
    }

    public String value() {
        return (String) innerGet();
    }
    public CmsObjectName value(String s) {
        if (s != null && s.length() > MAX_LEN)
            throw new IllegalArgumentException("CmsObjectName too long (max " + MAX_LEN + "): " + s.length());
        innerSet(s);
        return this;
    }
}
