package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
public class BrcbBuilder {

    /**
     * 构建 BRCB 控制块
     */
    public static CmsCBValue buildBrcb(SclReportControl rc) {
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
        return new CmsCBValue().selectBrcb();
    }
}
