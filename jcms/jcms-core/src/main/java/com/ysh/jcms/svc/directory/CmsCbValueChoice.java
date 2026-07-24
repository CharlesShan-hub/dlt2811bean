package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.block.CmsGoCb;
import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.data.block.CmsMsvcb;
import com.ysh.jcms.data.block.CmsSgcb;
import com.ysh.jcms.data.block.CmsUrcb;
import com.ysh.jcms.core.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * CBValue ::= CHOICE { brcb [0] IMPLICIT BRCB, urcb [1] IMPLICIT URCB, lcb [2]
 * IMPLICIT LCB, sgecb [3] IMPLICIT SGECB, gocb [4] IMPLICIT GOCB, msvcb [5]
 * IMPLICIT MSVCB } — 8.3.6
 *
 * Used by CBValueEntry in GetAllCBValues response.
 */
public class CmsCbValueChoice extends CmsTypeOld {

    public static final int BRCB = 0;
    public static final int URCB = 1;
    public static final int LCB = 2;
    public static final int SGECB = 3;
    public static final int GOCB = 4;
    public static final int MSVCB = 5;

    public CmsEnumerated choice; /* selector 0..5 */
    public CmsBrcb altBrcb;
    public CmsUrcb altUrcb;
    public CmsLcb altLcb;
    public CmsSgcb altSgecb;
    public CmsGoCb altGocb;
    public CmsMsvcb altMsvcb;

    public CmsCbValueChoice() {
        this.choice = new CmsEnumerated();
        this.altBrcb = new CmsBrcb();
        this.altUrcb = new CmsUrcb();
        this.altLcb = new CmsLcb();
        this.altSgecb = new CmsSgcb();
        this.altGocb = new CmsGoCb();
        this.altMsvcb = new CmsMsvcb();
    }

    public CmsCbValueChoice choice(int v) {
        this.choice.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(choice, altBrcb, altUrcb, altLcb, altSgecb, altGocb, altMsvcb);
    }
}
