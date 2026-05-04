package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetURCBValuesResultEntry extends AbstractCmsCompound<CmsSetURCBValuesResultEntry> {

    public CmsServiceError error = new CmsServiceError();
    public CmsServiceError rptID = new CmsServiceError();
    public CmsServiceError rptEna = new CmsServiceError();
    public CmsServiceError datSet = new CmsServiceError();
    public CmsServiceError optFlds = new CmsServiceError();
    public CmsServiceError bufTm = new CmsServiceError();
    public CmsServiceError trgOps = new CmsServiceError();
    public CmsServiceError intgPd = new CmsServiceError();
    public CmsServiceError gi = new CmsServiceError();
    public CmsServiceError resv = new CmsServiceError();

    public CmsSetURCBValuesResultEntry() {
        super("SetURCBValuesResultEntry");
        registerOptionalField("error");
        registerOptionalField("rptID");
        registerOptionalField("rptEna");
        registerOptionalField("datSet");
        registerOptionalField("optFlds");
        registerOptionalField("bufTm");
        registerOptionalField("trgOps");
        registerOptionalField("intgPd");
        registerOptionalField("gi");
        registerOptionalField("resv");
    }

    @Override
    public CmsSetURCBValuesResultEntry copy() {
        CmsSetURCBValuesResultEntry copy = new CmsSetURCBValuesResultEntry();
        copy.error = error.copy();
        copy.rptID = rptID.copy();
        copy.rptEna = rptEna.copy();
        copy.datSet = datSet.copy();
        copy.optFlds = optFlds.copy();
        copy.bufTm = bufTm.copy();
        copy.trgOps = trgOps.copy();
        copy.intgPd = intgPd.copy();
        copy.gi = gi.copy();
        copy.resv = resv.copy();
        return copy;
    }
}
