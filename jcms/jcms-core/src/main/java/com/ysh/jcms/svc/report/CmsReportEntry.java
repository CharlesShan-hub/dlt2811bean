package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.time.CmsBinaryTime;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * ReportEntry ::= SEQUENCE {
 *     timeOfEntry   [0] IMPLICIT EntryTime OPTIONAL,
 *     entryID       [1] IMPLICIT EntryID OPTIONAL,
 *     entryData     [2] IMPLICIT SEQUENCE OF ReportDataEntry
 * }  —  8.7.1
 *
 * Used by ReportPDU.
 */
public class CmsReportEntry extends CmsType {

    public CmsBoolean                            timeOfEntryPresent;
    public CmsBinaryTime                          timeOfEntry;       /* OPTIONAL */
    public CmsBoolean                            entryIdPresent;
    public CmsEntryId                            entryId;           /* OPTIONAL */
    public CmsArray<CmsReportDataEntry>          entryData;         /* SEQUENCE OF ReportDataEntry */

    public CmsReportEntry() {
        this.timeOfEntryPresent = new CmsBoolean();
        this.timeOfEntry        = new CmsBinaryTime();
        this.entryIdPresent     = new CmsBoolean();
        this.entryId            = new CmsEntryId();
        this.entryData          = new CmsArray<>(CmsReportDataEntry.class);
    }
    
    public CmsReportEntry timeOfEntryPresent(boolean v) { this.timeOfEntryPresent.value(v); return this; }
    public CmsReportEntry timeOfEntry(CmsBinaryTime v) { this.timeOfEntry = v; return this; }
    public CmsReportEntry entryIdPresent(boolean v) { this.entryIdPresent.value(v); return this; }
    public CmsReportEntry entryId(byte[] v) { this.entryIdPresent.value(v != null && v.length > 0); if (v != null) this.entryId.value(v); return this; }
    public CmsReportEntry entryId(String v) { this.entryIdPresent.value(v != null); if (v != null) this.entryId.value(v); return this; }
    public CmsReportEntry entryData(CmsArray<CmsReportDataEntry> v) { this.entryData = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(timeOfEntryPresent, timeOfEntry,
            entryIdPresent, entryId, entryData);
    }
}