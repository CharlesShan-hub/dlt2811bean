package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * LcbOptFlds ::= BIT STRING (SIZE(1)) — 7.6.5 PER: align + 1 byte (1 bit)
 */
public class CmsLcbOptFlds extends CmsType {

    public CmsBoolean value;

    public CmsLcbOptFlds() {
        super(Codec.LCB_OPT_FLDS);
        this.value = new CmsBoolean();
    }

    public CmsLcbOptFlds value(boolean v) {
        this.value.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(value);
    }
}
