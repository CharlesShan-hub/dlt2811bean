package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerTcmd;

/**
 * Tcmd ::= BIT STRING (SIZE(2)) — 7.3.7
 */
public class CmsTcmd extends CmsType {

    public static final int RESERVED = 0;
    public static final int SELECT = 1;
    public static final int OPERATE = 2;
    public static final int CANCEL = 3;

    public CmsTcmd() {
        super(new InnerTcmd());
    }
    public CmsTcmd(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerTcmd) inner).value;
    }
    public CmsTcmd value(int v) {
        if (v < 0 || v > 3)
            throw new IllegalArgumentException("CmsTcmd out of range [0,3]: " + v);
        ((InnerTcmd) inner).value = v;
        return this;
    }
}
