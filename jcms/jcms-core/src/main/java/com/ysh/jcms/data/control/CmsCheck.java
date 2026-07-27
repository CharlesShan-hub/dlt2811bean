package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsBitString;
import com.ysh.jcms.data.InnerCheck;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Check ::= BIT STRING (SIZE(2)) — 7.5.3
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsCheck extends CmsBitString {

    @Bit(0) public boolean syncheck;
    @Bit(1) public boolean interlock_check;

    public CmsCheck() { super(new InnerCheck()); }
}
