package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBinaryTime;
import com.ysh.dlt2811bean.datatypes.string.CmsEntryID;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsLogEntry extends AbstractCmsCompound<CmsLogEntry> {

    public CmsBinaryTime timeOfEntry = new CmsBinaryTime();
    public CmsEntryID entryID = new CmsEntryID();
    public CmsArray<CmsLogEntryData> entryData = new CmsArray<>(CmsLogEntryData::new).capacity(100);

    public CmsLogEntry() {
        super("LogEntry");
        registerField("timeOfEntry");
        registerField("entryID");
        registerField("entryData");
    }

    @Override
    public CmsLogEntry copy() {
        CmsLogEntry copy = new CmsLogEntry();
        copy.timeOfEntry = timeOfEntry.copy();
        copy.entryID = entryID.copy();
        copy.entryData = entryData.copy();
        return copy;
    }
}
