package com.ysh.dlt2811bean.service.svc.goose.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsGoCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorGocbChoice extends AbstractCmsChoice<CmsErrorGocbChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsGoCB gocb = new CmsGoCB();

    public CmsErrorGocbChoice() {
        super("ErrorGocbChoice", 0);
        registerAlternative("error");
        registerAlternative("gocb");
    }

    public CmsErrorGocbChoice selectError() { select(0); return this; }
    public CmsErrorGocbChoice selectGocb() { select(1); return this; }

    @Override
    public CmsErrorGocbChoice copy() {
        CmsErrorGocbChoice clone = new CmsErrorGocbChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.gocb = this.gocb.copy();
        return clone;
    }
}
