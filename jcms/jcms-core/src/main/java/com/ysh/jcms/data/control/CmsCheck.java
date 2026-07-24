package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerCheck;
import com.ysh.jcms.data.scalar.CmsBoolean;

/**
 * Check ::= BIT STRING (SIZE(2)) — 7.5.3
 * <p>
 * CmsCheck stores 2 boolean fields; InnerCheck packs them as a single int.
 */
public class CmsCheck extends CmsType {

    public CmsBoolean syncheck;
    public CmsBoolean interlock_check;

    public CmsCheck() {
        super(new InnerCheck());
        this.syncheck = new CmsBoolean();
        this.interlock_check = new CmsBoolean();
    }

    public CmsCheck(int value) {
        this();
        this.syncheck((value & (1 << InnerCheck.SYNCHECK)) != 0);
        this.interlock_check((value & (1 << InnerCheck.INTERLOCK_CHECK)) != 0);
    }

    public CmsCheck syncheck(boolean v) { this.syncheck.value(v); return this; }
    public CmsCheck interlock_check(boolean v) { this.interlock_check.value(v); return this; }

    @Override
    public void syncToInner() {
        int packed = 0;
        if (syncheck.value()) packed |= (1 << InnerCheck.SYNCHECK);
        if (interlock_check.value()) packed |= (1 << InnerCheck.INTERLOCK_CHECK);
        ((InnerCheck) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerCheck) inner).value;
        syncheck.value((packed >> InnerCheck.SYNCHECK & 1) != 0);
        interlock_check.value((packed >> InnerCheck.INTERLOCK_CHECK & 1) != 0);
    }
}
