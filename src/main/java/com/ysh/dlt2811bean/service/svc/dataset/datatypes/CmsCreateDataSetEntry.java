package com.ysh.dlt2811bean.service.svc.dataset.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsCreateDataSetEntry extends AbstractCmsCompound<CmsCreateDataSetEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsFC fc = new CmsFC();

    public CmsCreateDataSetEntry() {
        super("CreateDataSetEntry");
        registerField("reference");
        registerField("fc");
    }

    public CmsCreateDataSetEntry reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    public CmsCreateDataSetEntry fc(String fc) {
        this.fc.set(fc);
        return this;
    }

    @Override
    public CmsCreateDataSetEntry copy() {
        CmsCreateDataSetEntry copy = new CmsCreateDataSetEntry();
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        return copy;
    }
}