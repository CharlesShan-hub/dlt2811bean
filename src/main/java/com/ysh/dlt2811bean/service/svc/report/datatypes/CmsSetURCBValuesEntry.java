package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetURCBValuesEntry extends AbstractCmsCompound<CmsSetURCBValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsVisibleString rptID = new CmsVisibleString().max(129);
    public CmsBoolean rptEna = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsRcbOptFlds optFlds = new CmsRcbOptFlds();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsTriggerConditions trgOps = new CmsTriggerConditions();
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsBoolean resv = new CmsBoolean();

    public CmsSetURCBValuesEntry() {
        super("SetURCBValuesEntry");
        registerField("reference");
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
    public CmsSetURCBValuesEntry copy() {
        CmsSetURCBValuesEntry copy = new CmsSetURCBValuesEntry();
        copy.reference = reference.copy();
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
