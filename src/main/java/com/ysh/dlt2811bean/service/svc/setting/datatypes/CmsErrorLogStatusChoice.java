package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorLogStatusChoice extends AbstractCmsChoice<CmsErrorLogStatusChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsLogStatusValue value = new CmsLogStatusValue();

    public CmsErrorLogStatusChoice() {
        super("ErrorLogStatusChoice", 0);
        registerAlternative("error");
        registerAlternative("value");
    }

    public CmsErrorLogStatusChoice selectError() { select(0); return this; }
    public CmsErrorLogStatusChoice selectValue() { select(1); return this; }

    @Override
    public CmsErrorLogStatusChoice copy() {
        CmsErrorLogStatusChoice clone = new CmsErrorLogStatusChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.value = this.value.copy();
        return clone;
    }
}
