package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsUrcb;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;

/**
 * URCB 构建器
 */
public class UrcbBuilder {

    private final CmsUrcb urcb = new CmsUrcb();

    public UrcbBuilder rptId(String rptId) {
        if (rptId != null) urcb.rptID(rptId);
        return this;
    }

    public UrcbBuilder datSet(String datSet) {
        if (datSet != null) urcb.datSet(datSet);
        return this;
    }

    public UrcbBuilder confRev(long confRev) {
        urcb.confRev(confRev);
        return this;
    }

    public CmsCbValueChoice build() {
        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.URCB);
        result.altUrcb = urcb;
        return result;
    }

    public static CmsCbValueChoice from(SclReportControl rc) {
        return new UrcbBuilder()
                .rptId(rc.getRptID())
                .datSet(rc.getDatSet())
                .confRev(rc.getConfRev() != null ? Long.parseLong(rc.getConfRev()) : 0)
                .build();
    }
}
