package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.common.CmsEntryTime;
import java.util.Arrays;
import java.util.List;

/**
 * LogEntry ::= SEQUENCE {
 *     timeOfEntry     [0] IMPLICIT EntryTime,
 *     entryID         [1] IMPLICIT EntryID,
 *     entryData       [2] IMPLICIT SEQUENCE OF LogDataEntry
 * }  —  8.8.1
 */
public class CmsLogEntry extends CmsType {

    public CmsEntryTime               timeOfEntry;
    public CmsEntryId                 entryId;
    public CmsArray<CmsLogDataEntry>  entryData;   /* SEQUENCE OF LogDataEntry */

    public CmsLogEntry() {
        this.timeOfEntry = new CmsEntryTime();
        this.entryId     = new CmsEntryId();
        this.entryData   = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(timeOfEntry, entryId, entryData);
    }
}
