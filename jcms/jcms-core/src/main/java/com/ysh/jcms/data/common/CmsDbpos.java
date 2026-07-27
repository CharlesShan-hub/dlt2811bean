package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsEnum;
import com.ysh.jcms.data.InnerDbpos;

/**
 * Dbpos ::= BIT STRING (SIZE(2)) — 7.3.5
 */
@CmsEnum.ValueRange(min = 0, max = 3)
public class CmsDbpos extends CmsEnum<CmsDbpos> {

    public static final int INTERMEDIATE = 0;
    public static final int OFF = 1;
    public static final int ON = 2;
    public static final int BAD_STATE = 3;

    public CmsDbpos() { super(new InnerDbpos()); }
    public CmsDbpos(int v) { this(); value(v); }
}
