package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.common.CmsEntryTime;
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
    public CmsEntryTime                          timeOfEntry;       /* OPTIONAL */
    public CmsBoolean                            entryIdPresent;
    public CmsEntryId                            entryId;           /* OPTIONAL */
    public CmsArray<CmsReportDataEntry>          entryData;         /* SEQUENCE OF ReportDataEntry */

    public CmsReportEntry() {
        this.timeOfEntryPresent = new CmsBoolean();
        this.timeOfEntry        = new CmsEntryTime();
        this.entryIdPresent     = new CmsBoolean();
        this.entryId            = new CmsEntryId();
        this.entryData          = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(timeOfEntryPresent, timeOfEntry,
            entryIdPresent, entryId, entryData);
    }
}
