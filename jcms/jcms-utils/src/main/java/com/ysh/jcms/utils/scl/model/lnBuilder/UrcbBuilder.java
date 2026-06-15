package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsUrcb;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
public class UrcbBuilder {

    /**
     * 构建 URCB 控制块
     */
    public static CmsCbValueChoice buildUrcb(SclReportControl rc) {
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
        return new CmsCbValueChoice().selectUrcb();
    }
}
