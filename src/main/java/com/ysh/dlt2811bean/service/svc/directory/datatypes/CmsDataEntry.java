package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsDataEntry extends AbstractCmsCompound<CmsDataEntry> {

    public CmsSubReference reference = new CmsSubReference();
    public CmsData<?> value = new CmsData<>();

    public CmsDataEntry() {
        super("DataEntry");
        registerField("reference");
        registerField("value");
    }

    public CmsDataEntry reference(String ref) {
        this.reference = new CmsSubReference(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsDataEntry value(CmsType<?> val) {
        this.value = new CmsData(val);;
        return this;
    }

    @Override
    public CmsDataEntry copy() {
        CmsDataEntry copy = new CmsDataEntry();
        copy.reference = reference.copy();
        copy.value = value.copy();
        return copy;
    }
}