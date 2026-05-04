package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsLcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetLCBValuesEntry extends AbstractCmsCompound<CmsSetLCBValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsBoolean logEna = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsTriggerConditions trgOps = new CmsTriggerConditions();
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsObjectReference logRef = new CmsObjectReference();
    public CmsLcbOptFlds optFlds = new CmsLcbOptFlds();
    public CmsInt32U bufTm = new CmsInt32U();

    public CmsSetLCBValuesEntry() {
        super("SetLCBValuesEntry");
        registerField("reference");
        registerOptionalField("logEna");
        registerOptionalField("datSet");
        registerOptionalField("trgOps");
        registerOptionalField("intgPd");
        registerOptionalField("logRef");
        registerOptionalField("optFlds");
        registerOptionalField("bufTm");
    }

    @Override
    public CmsSetLCBValuesEntry copy() {
        CmsSetLCBValuesEntry copy = new CmsSetLCBValuesEntry();
        copy.reference = reference.copy();
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
