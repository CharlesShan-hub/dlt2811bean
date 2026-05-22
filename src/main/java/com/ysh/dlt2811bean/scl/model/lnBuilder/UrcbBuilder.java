package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
public class UrcbBuilder {

    /**
     * 构建 URCB 控制块
     */
    public static CmsCBValue buildUrcb(SclReportControl rc) {
        CmsURCB urcb = new CmsURCB();
        urcb.urcbName.set(rc.getName());
        if (rc.getDatSet() != null) {
            urcb.datSet.set(rc.getDatSet());
        }
        if (rc.getRptID() != null) {
            urcb.rptID.set(rc.getRptID());
        }
        if (rc.getConfRev() != null) {
            urcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        return new CmsCBValue().selectUrcb();
    }
}
