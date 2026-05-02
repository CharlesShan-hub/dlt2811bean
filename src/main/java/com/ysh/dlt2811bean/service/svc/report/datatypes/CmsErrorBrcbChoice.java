package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorBrcbChoice extends AbstractCmsChoice<CmsErrorBrcbChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsBRCB brcb = new CmsBRCB();

    public CmsErrorBrcbChoice() {
        super("ErrorBrcbChoice", 0);
        registerAlternative("error");
        registerAlternative("brcb");
    }

    public CmsErrorBrcbChoice selectError() { select(0); return this; }
    public CmsErrorBrcbChoice selectBrcb()  { select(1); return this; }

    @Override
    public CmsErrorBrcbChoice copy() {
        CmsErrorBrcbChoice clone = new CmsErrorBrcbChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.brcb = this.brcb.copy();
        return clone;
    }
}
