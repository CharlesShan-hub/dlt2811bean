package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.core.data.sequence.block.*;
import com.ysh.jcms.core.data.choice.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;

/**
 * Control block → CMS protocol type converter.
 * <p>
 * Converts control block definitions in the SCL model into {@link CmsCbValueChoice} protocol objects.
 */
public final class CbConverter {

    private CbConverter() {
    }

    // ==================== BRCB ====================

    public static CmsCbValueChoice brcbFrom(SclReportControl rc) {
        CmsBrcb brcb = new CmsBrcb();
        if (rc.rptID() != null)
            brcb.rptID(rc.rptID());
        if (rc.datSet() != null)
            brcb.datSet(rc.datSet());
        brcb.confRev(rc.confRev() != null ? Long.parseLong(rc.confRev()) : 0);

        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.BRCB);
        result.altBrcb = brcb;
        return result;
    }

    // ==================== URCB ====================

    public static CmsCbValueChoice urcbFrom(SclReportControl rc) {
        CmsUrcb urcb = new CmsUrcb();
        if (rc.rptID() != null)
            urcb.rptID(rc.rptID());
        if (rc.datSet() != null)
            urcb.datSet(rc.datSet());
        urcb.confRev(rc.confRev() != null ? Long.parseLong(rc.confRev()) : 0);

        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.URCB);
        result.altUrcb = urcb;
        return result;
    }

    // ==================== GOCB ====================

    public static CmsCbValueChoice gocbFrom(SclGSEControl gse) {
        CmsGoCb gocb = new CmsGoCb();
        if (gse.appID() != null)
            gocb.goID(gse.appID());
        if (gse.datSet() != null)
            gocb.datSet(gse.datSet());
        gocb.confRev(gse.confRev() != null ? Long.parseLong(gse.confRev()) : 0);

        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.GOCB);
        result.altGocb = gocb;
        return result;
    }

    // ==================== MSVCB ====================

    public static CmsCbValueChoice msvcbFrom(SclSampledValueControl sv) {
        CmsMsvcb msvcb = new CmsMsvcb();
        if (sv.svID() != null)
            msvcb.msvID(sv.svID());
        if (sv.datSet() != null)
            msvcb.datSet(sv.datSet());
        msvcb.confRev(sv.confRev() != null ? Long.parseLong(sv.confRev()) : 0);
        if (sv.smpRate() != null && !sv.smpRate().isEmpty()) {
            msvcb.smpRate(Integer.parseInt(sv.smpRate()));
        }

        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.MSVCB);
        result.altMsvcb = msvcb;
        return result;
    }

    // ==================== LCB ====================

    public static CmsCbValueChoice lcbFrom(SclLogControl lc) {
        CmsLcb lcb = new CmsLcb();
        if (lc.datSet() != null)
            lcb.datSet(lc.datSet());
        if (lc.logName() != null)
            lcb.logRef(lc.logName());

        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.LCB);
        result.altLcb = lcb;
        return result;
    }
}
