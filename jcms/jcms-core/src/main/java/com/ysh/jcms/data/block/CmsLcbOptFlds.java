package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsBitString;
import com.ysh.jcms.data.InnerLcbOptFlds;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * LcbOptFlds ::= BIT STRING (SIZE(1)) — 7.6.5
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsLcbOptFlds extends CmsBitString {

    @Bit(0) public boolean value;

    public CmsLcbOptFlds() {
        super(new InnerLcbOptFlds());
    }
}
