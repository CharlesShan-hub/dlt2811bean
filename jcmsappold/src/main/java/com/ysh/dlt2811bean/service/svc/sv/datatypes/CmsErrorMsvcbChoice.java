package com.ysh.dlt2811bean.service.svc.sv.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsMSVCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorMsvcbChoice extends AbstractCmsChoice<CmsErrorMsvcbChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsMSVCB msvcb = new CmsMSVCB();

    public CmsErrorMsvcbChoice() {
        super("ErrorMsvcbChoice", 0);
        registerAlternative("error");
        registerAlternative("msvcb");
    }

    public CmsErrorMsvcbChoice selectError() { select(0); return this; }
    public CmsErrorMsvcbChoice selectMsvcb() { select(1); return this; }

    @Override
    public CmsErrorMsvcbChoice copy() {
        CmsErrorMsvcbChoice clone = new CmsErrorMsvcbChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.msvcb = this.msvcb.copy();
        return clone;
    }
}
