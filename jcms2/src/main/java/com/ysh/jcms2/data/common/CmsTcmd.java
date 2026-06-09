package com.ysh.jcms2.data.common;

import com.ysh.jcms2.data.enumerated.CmsEnumerated;

/**
 * Tcmd ::= BIT STRING (SIZE(2))  —  7.3.7
 * PER: constrained integer (0..3), 2 bits
 * sizeof = 4
 *
 * Alias for CmsEnumerated with named constants.
 */
public class CmsTcmd extends CmsEnumerated {

    public static final int RESERVED = 0;
    public static final int SELECT   = 1;
    public static final int OPERATE  = 2;
    public static final int CANCEL   = 3;

    public CmsTcmd() {}
    public CmsTcmd(int value) { super(value); }
}
