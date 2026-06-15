package com.ysh.dlt2811bean.service.svc.data.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetDataValuesEntry extends AbstractCmsCompound<CmsSetDataValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsFC fc = new CmsFC();
    public CmsData<?> value = new CmsData<>();

    public CmsSetDataValuesEntry() {
        super("SetDataValuesEntry");
        registerField("reference");
        registerOptionalField("fc");
        registerField("value");
    }

    public CmsSetDataValuesEntry reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    public CmsSetDataValuesEntry fc(String fc) {
        this.fc.set(fc);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsSetDataValuesEntry value(CmsType<?> val) {
        this.value = new CmsData(val);
        return this;
    }

    @Override
    public CmsSetDataValuesEntry copy() {
        CmsSetDataValuesEntry copy = new CmsSetDataValuesEntry();
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        copy.value = value.copy();
        return copy;
    }
}