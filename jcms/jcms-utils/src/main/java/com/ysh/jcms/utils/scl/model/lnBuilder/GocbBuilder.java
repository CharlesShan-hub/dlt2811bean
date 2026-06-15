package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsGoCb;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
public class GocbBuilder {

    /**
     * 构建 GO_CB 控制块
     */
    public static CmsCbValueChoice buildGocb(SclGSEControl gse) {
        CmsGoCB gocb = new CmsGoCB();
        gocb.goCBName.set(gse.getName());
        if (gse.getDatSet() != null) {
            gocb.datSet.set(gse.getDatSet());
        }
        if (gse.getAppID() != null) {
            gocb.goID.set(gse.getAppID());
        }
        if (gse.getConfRev() != null) {
            gocb.confRev.set(Long.parseLong(gse.getConfRev()));
        }
        return new CmsCbValueChoice().selectGocb();
    }
}
