package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerLcbOptFlds;
import com.ysh.jcms.data.scalar.CmsBoolean;

/**
 * LcbOptFlds ::= BIT STRING (SIZE(1)) — 7.6.5
 * <p>
 * CmsLcbOptFlds stores 1 boolean field; InnerLcbOptFlds packs it as a single
 * int (bit 0 = refresh_time).
 */
public class CmsLcbOptFlds extends CmsType {

    public CmsBoolean value;

    public CmsLcbOptFlds() {
        super(new InnerLcbOptFlds());
        this.value = new CmsBoolean();
    }

    public CmsLcbOptFlds value(boolean v) { this.value.value(v); return this; }

    @Override
    public void syncToInner() {
        ((InnerLcbOptFlds) inner).value = value.value() ? 1 : 0;
    }

    @Override
    public void syncFromInner() {
        value.value((((InnerLcbOptFlds) inner).value & 1) != 0);
    }
}
