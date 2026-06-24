package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;

/**
 * BRCB 构建器
 */
public class BrcbBuilder {

    private final CmsBrcb brcb = new CmsBrcb();

    public BrcbBuilder rptId(String rptId) {
        if (rptId != null) brcb.rptID(rptId);
        return this;
    }

    public BrcbBuilder datSet(String datSet) {
        if (datSet != null) brcb.datSet(datSet);
        return this;
    }

    public BrcbBuilder confRev(long confRev) {
        brcb.confRev(confRev);
        return this;
    }

    public CmsCbValueChoice build() {
        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.BRCB);
        result.altBrcb = brcb;
        return result;
    }

    public static CmsCbValueChoice from(SclReportControl rc) {
        return new BrcbBuilder()
                .rptId(rc.getRptID())
                .datSet(rc.getDatSet())
                .confRev(rc.getConfRev() != null ? Long.parseLong(rc.getConfRev()) : 0)
                .build();
    }
}
