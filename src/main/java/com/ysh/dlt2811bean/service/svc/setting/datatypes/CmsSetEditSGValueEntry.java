package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetEditSGValueEntry extends AbstractCmsCompound<CmsSetEditSGValueEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsData value = new CmsData<>();

    public CmsSetEditSGValueEntry() {
        super("SetEditSGValueEntry");
        registerField("reference");
        registerField("value");
    }

    public CmsSetEditSGValueEntry reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsSetEditSGValueEntry value(CmsType<?> val) {
        this.value = new CmsData(val);
        return this;
    }

    @Override
    public CmsSetEditSGValueEntry copy() {
        CmsSetEditSGValueEntry copy = new CmsSetEditSGValueEntry();
        copy.reference = reference.copy();
        copy.value = value.copy();
        return copy;
    }
}
