package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsCBValueEntry extends AbstractCmsCompound<CmsCBValueEntry> {

    public CmsSubReference reference = new CmsSubReference();
    public CmsCBValue value = new CmsCBValue();

    public CmsCBValueEntry() {
        super("CBValueEntry");
        registerField("reference");
        registerField("value");
    }

    public CmsCBValueEntry reference(String ref) {
        this.reference = new CmsSubReference(ref);
        return this;
    }

    @Override
    public CmsCBValueEntry copy() {
        CmsCBValueEntry copy = new CmsCBValueEntry();
        copy.reference = reference.copy();
        copy.value = value.copy();
        return copy;
    }
}