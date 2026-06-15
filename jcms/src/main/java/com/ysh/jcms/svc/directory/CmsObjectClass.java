package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.enumerated.CmsEnumerated;

/**
 * ObjectClass ::= INTEGER {
 *     reserved        (0),
 *     logical-device  (1),
 *     file-system     (2)
 * } (0..2)  —  8.3.1
 * PER: constrained integer (0..2), 2 bits
 * sizeof = 4
 *
 * Alias for CmsEnumerated with named constants.
 */
public class CmsObjectClass extends CmsEnumerated {

    public static final int RESERVED        = 0;
    public static final int LOGICAL_DEVICE  = 1;
    public static final int FILE_SYSTEM     = 2;

    public CmsObjectClass() {}
    public CmsObjectClass(int value) { super(value); }
}
