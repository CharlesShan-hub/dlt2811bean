package com.ysh.jcms2.data.block;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * LcbOptFlds ::= BIT STRING (SIZE(1))  —  7.6.5
 * PER: align + 1 byte (1 bit)
 */
public class CmsLcbOptFlds extends CmsType {

    public CmsBoolean value;

    public CmsLcbOptFlds() {
        this.value = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(value);
    }
}
