package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerDbpos;

/**
 * Dbpos ::= BIT STRING (SIZE(2)) — 7.3.5
 */
public class CmsDbpos extends CmsType {

    public static final int INTERMEDIATE = 0;
    public static final int OFF = 1;
    public static final int ON = 2;
    public static final int BAD_STATE = 3;

    public CmsDbpos() {
        super(new InnerDbpos());
    }
    public CmsDbpos(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerDbpos) inner).value;
    }
    public CmsDbpos value(int v) {
        if (v < 0 || v > 3)
            throw new IllegalArgumentException("CmsDbpos out of range [0,3]: " + v);
        ((InnerDbpos) inner).value = v;
        return this;
    }
}
