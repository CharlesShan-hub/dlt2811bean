package com.ysh.dlt2811bean.service.svc.goose.datatypes;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetGoCBValuesEntry extends AbstractCmsCompound<CmsSetGoCBValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsBoolean goEna = new CmsBoolean();
    public CmsVisibleString goID = new CmsVisibleString().max(129);
    public CmsObjectReference datSet = new CmsObjectReference();

    public CmsSetGoCBValuesEntry() {
        super("SetGoCBValuesEntry");
        registerField("reference");
        registerOptionalField("goEna");
        registerOptionalField("goID");
        registerOptionalField("datSet");
    }

    @Override
    public CmsSetGoCBValuesEntry copy() {
        CmsSetGoCBValuesEntry copy = new CmsSetGoCBValuesEntry();
        copy.reference = reference.copy();
        copy.goEna = goEna.copy();
        copy.goID = goID.copy();
        copy.datSet = datSet.copy();
        return copy;
    }
}
