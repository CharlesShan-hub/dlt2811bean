package com.ysh.jcms.core.data.enumerate;

import com.ysh.jcms.core.data.core.CmsEnum;
import com.ysh.jcms.data.InnerACSIClass;

/**
 * <pre>
 * {@code
 * ACSIClass ::= INTEGER {
 *     reserved   (0),
 *     data-object (1),
 *     data-set   (2),
 *     brcb       (3),
 *     urcb       (4),
 *     lcb        (5),
 *     log        (6),
 *     sgcb       (7),
 *     gocb       (8),
 *     msvcb      (10)
 * } (0..10) — 8.3.3
 * }
 * </pre>
 *
 * <p>
 * PER: constrained integer (0..10), 4 bits.
 */
@CmsEnum.ValueRange(min = 0, max = 10)
public class CmsAcsiClass extends CmsEnum<CmsAcsiClass> {

    public static final int RESERVED = 0;
    public static final int DATA_OBJECT = 1;
    public static final int DATA_SET = 2;
    public static final int BRCB = 3;
    public static final int URCB = 4;
    public static final int LCB = 5;
    public static final int LOG = 6;
    public static final int SGCB = 7;
    public static final int GOCB = 8;
    public static final int MSVCB = 10;

    public CmsAcsiClass() {
        super(new InnerACSIClass());
    }
    public CmsAcsiClass(int value) {
        this();
        value(value);
    }
}
