package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsEnumerated;

/**
 * Dbpos ::= BIT STRING (SIZE(2))  —  7.3.5
 * PER: constrained integer (0..3), 2 bits
 * sizeof = 4
 *
 * Alias for CmsEnumerated with named constants.
 */
public class CmsDbpos extends CmsEnumerated {

    public static final int INTERMEDIATE = 0;
    public static final int OFF          = 1;
    public static final int ON           = 2;
    public static final int BAD_STATE    = 3;

    public CmsDbpos() {}
    public CmsDbpos(int value) { super(0, 3, value); }
}
