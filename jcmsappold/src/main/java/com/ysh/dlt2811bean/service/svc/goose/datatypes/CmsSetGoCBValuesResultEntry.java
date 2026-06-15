package com.ysh.dlt2811bean.service.svc.goose.datatypes;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetGoCBValuesResultEntry extends AbstractCmsCompound<CmsSetGoCBValuesResultEntry> {

    public CmsServiceError error = new CmsServiceError();
    public CmsServiceError goEna = new CmsServiceError();
    public CmsServiceError goID = new CmsServiceError();
    public CmsServiceError datSet = new CmsServiceError();

    public CmsSetGoCBValuesResultEntry() {
        super("SetGoCBValuesResultEntry");
        registerOptionalField("error");
        registerOptionalField("goEna");
        registerOptionalField("goID");
        registerOptionalField("datSet");
    }

    @Override
    public CmsSetGoCBValuesResultEntry copy() {
        CmsSetGoCBValuesResultEntry copy = new CmsSetGoCBValuesResultEntry();
        copy.error = error.copy();
        copy.goEna = goEna.copy();
        copy.goID = goID.copy();
        copy.datSet = datSet.copy();
        return copy;
    }
}
