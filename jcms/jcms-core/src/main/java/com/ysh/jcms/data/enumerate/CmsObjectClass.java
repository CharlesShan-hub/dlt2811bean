package com.ysh.jcms.data.enumerate;

import com.ysh.jcms.data.core.CmsEnum;

/**
 * <pre>
 * {@code
 * ObjectClass ::= INTEGER {
 *     reserved       (0),
 *     logical-device (1),
 *     file-system    (2)
 * } (0..2) — 8.3.1
 * }
 * </pre>
 *
 * <p>
 * PER: constrained integer (0..2), 2 bits.
 */
@CmsEnum.ValueRange(min = 0, max = 2)
public class CmsObjectClass extends CmsEnum<CmsObjectClass> {

    public static final int RESERVED = 0;
    public static final int LOGICAL_DEVICE = 1;
    public static final int FILE_SYSTEM = 2;

    public CmsObjectClass() {
    }
    public CmsObjectClass(int value) {
        value(value);
    }
}
