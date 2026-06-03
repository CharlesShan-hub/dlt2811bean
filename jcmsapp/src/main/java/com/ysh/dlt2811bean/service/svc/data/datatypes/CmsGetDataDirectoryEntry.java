package com.ysh.dlt2811bean.service.svc.data.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsGetDataDirectoryEntry extends AbstractCmsCompound<CmsGetDataDirectoryEntry> {

    public CmsSubReference reference = new CmsSubReference();
    public CmsFC fc = new CmsFC();

    public CmsGetDataDirectoryEntry() {
        super("GetDataDirectoryEntry");
        registerField("reference");
        registerOptionalField("fc");
    }

    public CmsGetDataDirectoryEntry reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    public CmsGetDataDirectoryEntry fc(String fc) {
        this.fc.set(fc);
        return this;
    }

    @Override
    public CmsGetDataDirectoryEntry copy() {
        CmsGetDataDirectoryEntry copy = new CmsGetDataDirectoryEntry();
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        return copy;
    }
}