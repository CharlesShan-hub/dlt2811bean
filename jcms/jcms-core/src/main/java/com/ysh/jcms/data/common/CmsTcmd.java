package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsEnum;
import com.ysh.jcms.data.InnerTcmd;

/**
 * Tcmd ::= BIT STRING (SIZE(2)) — 7.3.7
 */
@CmsEnum.ValueRange(min = 0, max = 3)
public class CmsTcmd extends CmsEnum<CmsTcmd> {

    public static final int RESERVED = 0;
    public static final int SELECT = 1;
    public static final int OPERATE = 2;
    public static final int CANCEL = 3;

    public CmsTcmd() { super(new InnerTcmd()); }
    public CmsTcmd(int v) { this(); value(v); }
}
