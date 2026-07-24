package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsEnumerated;
import com.ysh.jcms.data.InnerDbpos;

/**
 * Dbpos ::= BIT STRING (SIZE(2)) — 7.3.5 PER: constrained integer (0..3), 2
 * bits sizeof = 4
 *
 * Alias for CmsEnumerated with named constants.
 */
public class CmsDbpos extends CmsEnumerated {

    public static final int INTERMEDIATE = 0;
    public static final int OFF = 1;
    public static final int ON = 2;
    public static final int BAD_STATE = 3;

    private transient InnerDbpos inner = new InnerDbpos();

    public CmsDbpos() {
        super(0, 3, INTERMEDIATE);
    }
    public CmsDbpos(int value) {
        super(0, 3, value);
    }

    @Override
    public byte[] encode() {
        inner.value = value();
        return inner.encode();
    }

    @Override
    public void decode(byte[] data) {
        inner = InnerDbpos.decode(data);
        value(inner.value);
    }
}
