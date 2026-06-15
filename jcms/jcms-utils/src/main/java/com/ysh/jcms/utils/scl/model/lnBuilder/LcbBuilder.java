package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
public class LcbBuilder {

    /**
     * 构建 LCB 控制块
     */
    public static CmsCbValueChoice buildLcb(SclLogControl lc) {
        CmsLCB lcb = new CmsLCB();
        lcb.lcbName.set(lc.getName());
        if (lc.getDatSet() != null) {
            lcb.datSet.set(lc.getDatSet());
        }
        if (lc.getLogName() != null) {
            lcb.logRef.set(lc.getLogName());
        }
        return new CmsCbValueChoice().selectLcb();
    }
}
