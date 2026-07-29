package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.sequence.block.CmsGoCb;
import com.ysh.jcms.data.sequence.block.CmsLcb;
import com.ysh.jcms.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.data.sequence.block.CmsSgcb;
import com.ysh.jcms.data.sequence.block.CmsUrcb;

/**
 * CBValue ::= CHOICE { brcb [0] IMPLICIT BRCB, urcb [1] IMPLICIT URCB, lcb [2]
 * IMPLICIT LCB, sgecb [3] IMPLICIT SGECB, gocb [4] IMPLICIT GOCB, msvcb [5]
 * IMPLICIT MSVCB } — 8.3.6
 */
public class CmsCbValueChoice extends CmsChoice {

    public static final int BRCB = 0;
    public static final int URCB = 1;
    public static final int LCB = 2;
    public static final int SGECB = 3;
    public static final int GOCB = 4;
    public static final int MSVCB = 5;

    @Choice(index = 0, name = "brcb",    sync = Sync.WRAPPER, innerField = "brcb")  public CmsBrcb altBrcb;
    @Choice(index = 1, name = "urcb",    sync = Sync.WRAPPER, innerField = "urcb")  public CmsUrcb altUrcb;
    @Choice(index = 2, name = "lcb",     sync = Sync.WRAPPER, innerField = "lcb")   public CmsLcb altLcb;
    @Choice(index = 3, name = "sgecb",   sync = Sync.WRAPPER, innerField = "sgcb")  public CmsSgcb altSgecb;
    @Choice(index = 4, name = "gocb",    sync = Sync.WRAPPER, innerField = "gocb")  public CmsGoCb altGocb;
    @Choice(index = 5, name = "msvcb",   sync = Sync.WRAPPER, innerField = "msvcb") public CmsMsvcb altMsvcb;

    public CmsCbValueChoice() {
        super(new InnerEmpty());
    }

    public CmsCbValueChoice choice(int v) { super.choice(v); return this; }
}
