package com.ysh.dlt2811bean.service.svc.sv.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsMsvcbOptFlds;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsSmpMod;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsSetMSVCBValuesEntry extends AbstractCmsCompound<CmsSetMSVCBValuesEntry> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsBoolean svEna = new CmsBoolean();
    public CmsVisibleString msvID = new CmsVisibleString().max(129);
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsSmpMod smpMod = new CmsSmpMod();
    public CmsInt16U smpRate = new CmsInt16U();
    public CmsMsvcbOptFlds optFlds = new CmsMsvcbOptFlds();

    public CmsSetMSVCBValuesEntry() {
        super("SetMSVCBValuesEntry");
        registerField("reference");
        registerOptionalField("svEna");
        registerOptionalField("msvID");
        registerOptionalField("datSet");
        registerOptionalField("smpMod");
        registerOptionalField("smpRate");
        registerOptionalField("optFlds");
    }

    @Override
    public CmsSetMSVCBValuesEntry copy() {
        CmsSetMSVCBValuesEntry copy = new CmsSetMSVCBValuesEntry();
        copy.reference = reference.copy();
        copy.svEna = svEna.copy();
        copy.msvID = msvID.copy();
        copy.datSet = datSet.copy();
        copy.smpMod = smpMod.copy();
        copy.smpRate = smpRate.copy();
        copy.optFlds = optFlds.copy();
        return copy;
    }
}
