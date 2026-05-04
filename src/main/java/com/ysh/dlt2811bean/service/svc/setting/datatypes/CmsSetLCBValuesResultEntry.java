package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetLCBValuesResultEntry extends AbstractCmsCompound<CmsSetLCBValuesResultEntry> {

    public CmsServiceError error = new CmsServiceError();
    public CmsServiceError logEna = new CmsServiceError();
    public CmsServiceError datSet = new CmsServiceError();
    public CmsServiceError trgOps = new CmsServiceError();
    public CmsServiceError intgPd = new CmsServiceError();
    public CmsServiceError logRef = new CmsServiceError();
    public CmsServiceError optFlds = new CmsServiceError();
    public CmsServiceError bufTm = new CmsServiceError();

    public CmsSetLCBValuesResultEntry() {
        super("SetLCBValuesResultEntry");
        registerField("error");
        registerOptionalField("logEna");
        registerOptionalField("datSet");
        registerOptionalField("trgOps");
        registerOptionalField("intgPd");
        registerOptionalField("logRef");
        registerOptionalField("optFlds");
        registerOptionalField("bufTm");
    }

    @Override
    public CmsSetLCBValuesResultEntry copy() {
        CmsSetLCBValuesResultEntry copy = new CmsSetLCBValuesResultEntry();
        copy.error = error.copy();
        copy.logEna = logEna.copy();
        copy.datSet = datSet.copy();
        copy.trgOps = trgOps.copy();
        copy.intgPd = intgPd.copy();
        copy.logRef = logRef.copy();
        copy.optFlds = optFlds.copy();
        copy.bufTm = bufTm.copy();
        return copy;
    }
}
