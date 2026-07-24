package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.time.CmsBinaryTime;
import java.util.Arrays;
import java.util.List;

/**
 * LogEntry ::= SEQUENCE { timeOfEntry [0] IMPLICIT EntryTime, entryID [1]
 * IMPLICIT EntryID, entryData [2] IMPLICIT SEQUENCE OF LogDataEntry } — 8.8.1
 */
public class CmsLogEntry extends CmsTypeOld {

    public CmsBinaryTime timeOfEntry;
    public CmsEntryId entryId;
    public CmsArray<CmsLogDataEntry> entryData; /* SEQUENCE OF LogDataEntry */

    public CmsLogEntry() {
        this.timeOfEntry = new CmsBinaryTime();
        this.entryId = new CmsEntryId();
        this.entryData = new CmsArray<>(CmsLogDataEntry.class);
    }

    public CmsLogEntry timeOfEntry(CmsBinaryTime v) {
        this.timeOfEntry = v;
        return this;
    }
    public CmsLogEntry entryId(byte[] v) {
        this.entryId.value(v);
        return this;
    }
    public CmsLogEntry entryId(String v) {
        this.entryId.value(v);
        return this;
    }
    public CmsLogEntry entryData(CmsArray<CmsLogDataEntry> v) {
        this.entryData = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(timeOfEntry, entryId, entryData);
    }
}
