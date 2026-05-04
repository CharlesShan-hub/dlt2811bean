package com.ysh.dlt2811bean.service.svc.rpc.datatypes;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsErrorMethodChoice extends AbstractCmsChoice<CmsErrorMethodChoice> {

    public CmsServiceError error = new CmsServiceError();
    public CmsRpcMethodValue method = new CmsRpcMethodValue();

    public CmsErrorMethodChoice() {
        super("ErrorMethodChoice", 0);
        registerAlternative("error");
        registerAlternative("method");
    }

    public CmsErrorMethodChoice selectError() { select(0); return this; }
    public CmsErrorMethodChoice selectMethod() { select(1); return this; }

    @Override
    public CmsErrorMethodChoice copy() {
        CmsErrorMethodChoice clone = new CmsErrorMethodChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.error = this.error.copy();
        clone.method = this.method.copy();
        return clone;
    }
}
