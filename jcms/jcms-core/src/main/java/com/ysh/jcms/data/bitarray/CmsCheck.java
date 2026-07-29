package com.ysh.jcms.data.bitarray;

import com.ysh.jcms.data.core.CmsBits;
import com.ysh.jcms.data.InnerCheck;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Check ::= BIT STRING (SIZE(2)) — 7.5.3
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsCheck extends CmsBits {

    @Bit(0) public boolean syncheck;
    @Bit(1) public boolean interlock_check;

    public CmsCheck() { super(new InnerCheck()); }
}
