package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsLCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorLcbChoice extends AbstractCmsChoice<CmsErrorLcbChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsLCB value = new CmsLCB();

    public CmsErrorLcbChoice() {
        super("ErrorLcbChoice", 0);
        registerAlternative("error");
        registerAlternative("value");
    }

    public CmsErrorLcbChoice selectError() { select(0); return this; }
    public CmsErrorLcbChoice selectValue() { select(1); return this; }

    @Override
    public CmsErrorLcbChoice copy() {
        CmsErrorLcbChoice clone = new CmsErrorLcbChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.value = this.value.copy();
        return clone;
    }
}
