package com.ysh.dlt2811bean.service.svc.data.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsGetDataValuesEntry extends AbstractCmsCompound<CmsGetDataValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsFC fc = new CmsFC();

    public CmsGetDataValuesEntry() {
        super("GetDataValuesEntry");
        registerField("reference");
        registerOptionalField("fc");
    }

    public CmsGetDataValuesEntry reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    public CmsGetDataValuesEntry fc(String fc) {
        this.fc.set(fc);
        return this;
    }

    @Override
    public CmsGetDataValuesEntry copy() {
        CmsGetDataValuesEntry copy = new CmsGetDataValuesEntry();
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        return copy;
    }
}