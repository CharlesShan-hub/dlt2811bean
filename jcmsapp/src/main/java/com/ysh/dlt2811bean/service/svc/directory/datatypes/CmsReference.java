package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsReference extends AbstractCmsChoice<CmsReference> {

    public CmsObjectName ldName = new CmsObjectName();
    public CmsObjectReference lnReference = new CmsObjectReference();

    public CmsReference() {
        super("CmsReference", 0);
        registerAlternative("ldName");
        registerAlternative("lnReference");
    }

    public CmsReference ldName(String name) {
        select(0);
        this.ldName.set(name);
        return this;
    }

    public CmsReference lnReference(String ref) {
        select(1);
        this.lnReference.set(ref);
        return this;
    }

    @Override
    public CmsReference copy() {
        CmsReference clone = new CmsReference();
        clone.selectedIndex = this.selectedIndex;
        clone.ldName = this.ldName.copy();
        clone.lnReference = this.lnReference.copy();
        return clone;
    }
}