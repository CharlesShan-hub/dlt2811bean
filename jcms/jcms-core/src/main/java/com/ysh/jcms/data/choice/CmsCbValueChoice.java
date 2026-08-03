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
 * <pre>
 * {@code
 * CBValue ::= CHOICE {
 *     brcb  [0] IMPLICIT BRCB,
 *     urcb  [1] IMPLICIT URCB,
 *     lcb   [2] IMPLICIT LCB,
 *     sgcb  [3] IMPLICIT SGCB,
 *     gocb  [4] IMPLICIT GOCB,
 *     msvcb [5] IMPLICIT MSVCB
 * } — 8.3.6
 * }
 * </pre>
 */
public class CmsCbValueChoice extends CmsChoice {

    public static final int BRCB = 0;
    public static final int URCB = 1;
    public static final int LCB = 2;
    public static final int SGCB = 3;
    public static final int GOCB = 4;
    public static final int MSVCB = 5;

    @Choice(index = 0, name = "brcb", sync = Sync.WRAPPER, innerField = "brcb")
    public CmsBrcb altBrcb;
    @Choice(index = 1, name = "urcb", sync = Sync.WRAPPER, innerField = "urcb")
    public CmsUrcb altUrcb;
    @Choice(index = 2, name = "lcb", sync = Sync.WRAPPER, innerField = "lcb")
    public CmsLcb altLcb;
    @Choice(index = 3, name = "sgcb", sync = Sync.WRAPPER, innerField = "sgcb")
    public CmsSgcb altSgcb;
    @Choice(index = 4, name = "gocb", sync = Sync.WRAPPER, innerField = "gocb")
    public CmsGoCb altGocb;
    @Choice(index = 5, name = "msvcb", sync = Sync.WRAPPER, innerField = "msvcb")
    public CmsMsvcb altMsvcb;

    public CmsCbValueChoice() {
        super(new InnerEmpty());
    }

    public CmsCbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsCbValueChoice altBrcb(CmsBrcb v) {
        choice(BRCB);
        this.altBrcb.value(v);
        return this;
    }
    public CmsCbValueChoice altUrcb(CmsUrcb v) {
        choice(URCB);
        this.altUrcb.value(v);
        return this;
    }
    public CmsCbValueChoice altLcb(CmsLcb v) {
        choice(LCB);
        this.altLcb.value(v);
        return this;
    }
    public CmsCbValueChoice altSgcb(CmsSgcb v) {
        choice(SGCB);
        this.altSgcb.value(v);
        return this;
    }
    public CmsCbValueChoice altGocb(CmsGoCb v) {
        choice(GOCB);
        this.altGocb.value(v);
        return this;
    }
    public CmsCbValueChoice altMsvcb(CmsMsvcb v) {
        choice(MSVCB);
        this.altMsvcb.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsCbValueChoice (fluent). */
    public CmsCbValueChoice value(CmsCbValueChoice v) {
        switch (v.choice()) {
            case BRCB :
                return altBrcb(v.altBrcb);
            case URCB :
                return altUrcb(v.altUrcb);
            case LCB :
                return altLcb(v.altLcb);
            case SGCB :
                return altSgcb(v.altSgcb);
            case GOCB :
                return altGocb(v.altGocb);
            case MSVCB :
                return altMsvcb(v.altMsvcb);
            default :
                throw new IllegalArgumentException("Unknown CBValue choice: " + v.choice());
        }
    }
}
