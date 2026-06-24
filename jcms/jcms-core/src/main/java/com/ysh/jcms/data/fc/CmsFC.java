package com.ysh.jcms.data.fc;

import com.ysh.jcms.core.CmsEnumerated;
/**
 * FunctionalConstraint ::= VisibleString (SIZE(2))  —  7.4
 *
 * PER wire format is VisibleString(SIZE(2)) — 2 ASCII chars.
 * Java API exposes FC as an int value mapped to 2-char FC codes
 * via {@link com.ysh.jcms.info.FunctionalConstraint}.
 *
 * @see com.ysh.jcms.info.FunctionalConstraint
 */
public class CmsFC extends CmsEnumerated {

    public static final int ST  = 0;
    public static final int MX  = 1;
    public static final int SP  = 2;
    public static final int SV  = 3;
    public static final int CF  = 4;
    public static final int DC  = 5;
    public static final int SG  = 6;
    public static final int SE  = 7;
    public static final int SR  = 8;
    public static final int OR  = 9;
    public static final int BL  = 10;
    public static final int EX  = 11;
    public static final int XX  = 12;

    public CmsFC() { super(0, 12, ST); }

    public CmsFC(int value) { super(0, 12, value); }

    @Override
    public CmsFC value(int v) { return (CmsFC) super.value(v); }
}
