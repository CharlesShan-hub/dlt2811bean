package com.ysh.dlt2811bean.service.svc.sv.datatypes;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetMSVCBValuesResultEntry extends AbstractCmsCompound<CmsSetMSVCBValuesResultEntry> {

    public CmsServiceError error = new CmsServiceError();
    public CmsServiceError svEna = new CmsServiceError();
    public CmsServiceError msvID = new CmsServiceError();
    public CmsServiceError datSet = new CmsServiceError();
    public CmsServiceError smpMod = new CmsServiceError();
    public CmsServiceError smpRate = new CmsServiceError();
    public CmsServiceError optFlds = new CmsServiceError();

    public CmsSetMSVCBValuesResultEntry() {
        super("SetMSVCBValuesResultEntry");
        registerOptionalField("error");
        registerOptionalField("svEna");
        registerOptionalField("msvID");
        registerOptionalField("datSet");
        registerOptionalField("smpMod");
        registerOptionalField("smpRate");
        registerOptionalField("optFlds");
    }

    @Override
    public CmsSetMSVCBValuesResultEntry copy() {
        CmsSetMSVCBValuesResultEntry copy = new CmsSetMSVCBValuesResultEntry();
        copy.error = error.copy();
        copy.svEna = svEna.copy();
        copy.msvID = msvID.copy();
        copy.datSet = datSet.copy();
        copy.smpMod = smpMod.copy();
        copy.smpRate = smpRate.copy();
        copy.optFlds = optFlds.copy();
        return copy;
    }
}
