package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.datatypes.compound.CmsLCB;
import com.ysh.dlt2811bean.scl.model.control.SclLogControl;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
public class LcbBuilder {

    /**
     * 构建 LCB 控制块
     */
    public static CmsCBValue buildLcb(SclLogControl lc) {
        CmsLCB lcb = new CmsLCB();
        lcb.lcbName.set(lc.getName());
        if (lc.getDatSet() != null) {
            lcb.datSet.set(lc.getDatSet());
        }
        if (lc.getLogName() != null) {
            lcb.logRef.set(lc.getLogName());
        }
        return new CmsCBValue().selectLcb();
    }
}
