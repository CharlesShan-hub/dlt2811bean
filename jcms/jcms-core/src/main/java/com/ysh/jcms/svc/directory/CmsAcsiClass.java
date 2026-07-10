package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsEnumerated;

/**
 * ACSIClass ::= INTEGER { reserved (0), data-object (1), data-set (2), brcb
 * (3), urcb (4), lcb (5), log (6), sgecb (7), gocb (8), msvcb (10) } (0..10) —
 * 8.3.3 PER: constrained integer (0..10), 4 bits sizeof = 4
 *
 * Alias for CmsEnumerated with named constants.
 */
public class CmsAcsiClass extends CmsEnumerated {

    public static final int RESERVED = 0;
    public static final int DATA_OBJECT = 1;
    public static final int DATA_SET = 2;
    public static final int BRCB = 3;
    public static final int URCB = 4;
    public static final int LCB = 5;
    public static final int LOG = 6;
    public static final int SGECB = 7;
    public static final int GOCB = 8;
    public static final int MSVCB = 10;

    public CmsAcsiClass() {
    }
    public CmsAcsiClass(int value) {
        super(value);
    }
}
