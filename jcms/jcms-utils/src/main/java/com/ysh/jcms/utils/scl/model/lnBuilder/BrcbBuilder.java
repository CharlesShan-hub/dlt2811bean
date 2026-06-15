package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
public class BrcbBuilder {

    /**
     * 构建 BRCB 控制块
     */
    public static CmsCbValueChoice buildBrcb(SclReportControl rc) {
        CmsBRCB brcb = new CmsBRCB();
        brcb.brcbName.set(rc.getName());
        if (rc.getDatSet() != null) {
            brcb.datSet.set(rc.getDatSet());
        }
        if (rc.getRptID() != null) {
            brcb.rptID.set(rc.getRptID());
        }
        if (rc.getConfRev() != null) {
            brcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        return new CmsCbValueChoice().selectBrcb();
    }
}
