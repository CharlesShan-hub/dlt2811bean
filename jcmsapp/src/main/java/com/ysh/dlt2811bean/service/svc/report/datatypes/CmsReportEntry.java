package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBinaryTime;
import com.ysh.dlt2811bean.datatypes.string.CmsEntryID;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsReportEntry extends AbstractCmsCompound<CmsReportEntry> {

    public CmsBinaryTime timeOfEntry = new CmsBinaryTime();
    public CmsEntryID entryID = new CmsEntryID();
    public CmsArray<CmsReportEntryData> entryData = new CmsArray<>(CmsReportEntryData::new);

    public CmsReportEntry() {
        super("ReportEntry");
        registerOptionalField("timeOfEntry");
        registerOptionalField("entryID");
        registerField("entryData");
    }

    @Override
    public CmsReportEntry copy() {
        CmsReportEntry copy = new CmsReportEntry();
        copy.timeOfEntry = timeOfEntry.copy();
        copy.entryID = entryID.copy();
        copy.entryData = entryData.copy();
        return copy;
    }
}
