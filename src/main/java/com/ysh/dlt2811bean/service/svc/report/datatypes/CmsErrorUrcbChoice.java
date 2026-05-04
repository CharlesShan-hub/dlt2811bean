package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorUrcbChoice extends AbstractCmsChoice<CmsErrorUrcbChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsURCB value = new CmsURCB();

    public CmsErrorUrcbChoice() {
        super("ErrorUrcbChoice", 0);
        registerAlternative("error");
        registerAlternative("value");
    }

    public CmsErrorUrcbChoice selectError() { select(0); return this; }
    public CmsErrorUrcbChoice selectValue() { select(1); return this; }

    @Override
    public CmsErrorUrcbChoice copy() {
        CmsErrorUrcbChoice clone = new CmsErrorUrcbChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.value = this.value.copy();
        return clone;
    }
}
