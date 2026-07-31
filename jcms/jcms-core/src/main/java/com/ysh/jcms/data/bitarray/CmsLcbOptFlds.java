package com.ysh.jcms.data.bitarray;

import com.ysh.jcms.data.core.CmsBits;
import com.ysh.jcms.data.InnerLcbOptFlds;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * LcbOptFlds ::= BIT STRING (SIZE(1)) — 7.6.5
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsLcbOptFlds extends CmsBits {

    @Bit(0) public boolean bit0;

    public CmsLcbOptFlds() {
        super(new InnerLcbOptFlds());
    }
}
