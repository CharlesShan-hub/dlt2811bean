package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsEntryID;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetBRCBValuesEntry extends AbstractCmsCompound<CmsSetBRCBValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsVisibleString rptID = new CmsVisibleString().max(129);
    public CmsBoolean rptEna = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsRcbOptFlds optFlds = new CmsRcbOptFlds();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsTriggerConditions trgOps = new CmsTriggerConditions();
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsBoolean purgeBuf = new CmsBoolean();
    public CmsEntryID entryID = new CmsEntryID();
    public CmsInt16 resvTms = new CmsInt16();

    public CmsSetBRCBValuesEntry() {
        super("SetBRCBValuesEntry");
        registerField("reference");
        registerOptionalField("rptID");
        registerOptionalField("rptEna");
        registerOptionalField("datSet");
        registerOptionalField("optFlds");
        registerOptionalField("bufTm");
        registerOptionalField("trgOps");
        registerOptionalField("intgPd");
        registerOptionalField("gi");
        registerOptionalField("purgeBuf");
        registerOptionalField("entryID");
        registerOptionalField("resvTms");
    }

    @Override
    public CmsSetBRCBValuesEntry copy() {
        CmsSetBRCBValuesEntry copy = new CmsSetBRCBValuesEntry();
        copy.reference = reference.copy();
        copy.rptID = rptID.copy();
        copy.rptEna = rptEna.copy();
        copy.datSet = datSet.copy();
        copy.optFlds = optFlds.copy();
        copy.bufTm = bufTm.copy();
        copy.trgOps = trgOps.copy();
        copy.intgPd = intgPd.copy();
        copy.gi = gi.copy();
        copy.purgeBuf = purgeBuf.copy();
        copy.entryID = entryID.copy();
        copy.resvTms = resvTms.copy();
        return copy;
    }
}
