package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.datatypes.compound.CmsGoCB;
import com.ysh.dlt2811bean.scl.model.control.SclGSEControl;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
public class GocbBuilder {

    /**
     * 构建 GO_CB 控制块
     */
    public static CmsCBValue buildGocb(SclGSEControl gse) {
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
        return new CmsCBValue().selectGocb();
    }
}
