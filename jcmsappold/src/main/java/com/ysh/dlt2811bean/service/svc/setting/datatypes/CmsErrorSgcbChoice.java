package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorSgcbChoice extends AbstractCmsChoice<CmsErrorSgcbChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsSGCB sgcb = new CmsSGCB();

    public CmsErrorSgcbChoice() {
        super("ErrorSgcbChoice", 0);
        registerAlternative("error");
        registerAlternative("sgcb");
    }

    public CmsErrorSgcbChoice selectError() { select(0); return this; }
    public CmsErrorSgcbChoice selectSgcb()  { select(1); return this; }

    @Override
    public CmsErrorSgcbChoice copy() {
        CmsErrorSgcbChoice clone = new CmsErrorSgcbChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.sgcb = this.sgcb.copy();
        return clone;
    }
}
