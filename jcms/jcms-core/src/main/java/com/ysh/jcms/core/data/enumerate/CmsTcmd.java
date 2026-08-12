package com.ysh.jcms.core.data.enumerate;

import com.ysh.jcms.data.InnerTcmd;
import com.ysh.jcms.core.data.core.CmsEnum;

/**
 * <pre>
 * {@code
 * Tcmd ::= BIT STRING {
 *     reserved (0),
 *     select   (1),
 *     operate  (2),
 *     cancel   (3)
 * } (SIZE(2)) — 7.3.7
 * }
 * </pre>
 */
@CmsEnum.ValueRange(min = 0, max = 3)
public class CmsTcmd extends CmsEnum<CmsTcmd> {

    public static final int RESERVED = 0;
    public static final int SELECT = 1;
    public static final int OPERATE = 2;
    public static final int CANCEL = 3;

    public CmsTcmd() {
        super(new InnerTcmd());
    }
    public CmsTcmd(int v) {
        this();
        value(v);
    }
}
