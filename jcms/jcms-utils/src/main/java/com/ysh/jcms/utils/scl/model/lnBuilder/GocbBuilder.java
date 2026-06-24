package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsGoCb;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;

/**
 * GO_CB 构建器
 */
public class GocbBuilder {

    private final CmsGoCb gocb = new CmsGoCb();

    public GocbBuilder goId(String goId) {
        if (goId != null) gocb.goID(goId);
        return this;
    }

    public GocbBuilder datSet(String datSet) {
        if (datSet != null) gocb.datSet(datSet);
        return this;
    }

    public GocbBuilder confRev(long confRev) {
        gocb.confRev(confRev);
        return this;
    }

    public CmsCbValueChoice build() {
        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.GOCB);
        result.altGocb = gocb;
        return result;
    }

    public static CmsCbValueChoice from(SclGSEControl gse) {
        return new GocbBuilder()
                .goId(gse.getAppID())
                .datSet(gse.getDatSet())
                .confRev(gse.getConfRev() != null ? Long.parseLong(gse.getConfRev()) : 0)
                .build();
    }
}
