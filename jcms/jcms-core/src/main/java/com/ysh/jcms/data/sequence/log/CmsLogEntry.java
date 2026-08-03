package com.ysh.jcms.data.sequence.log;

import com.ysh.jcms.data.InnerLogEntry;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * LogEntry ::= SEQUENCE {
 *     timeOfEntry     [0] IMPLICIT EntryTime,
 *     entryID         [1] IMPLICIT EntryID,
 *     entryData       [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         fc          [1] IMPLICIT FunctionalConstraint,
 *         value       [2] IMPLICIT Data,
 *         reason      [3] IMPLICIT ReasonCode
 *     }
 * } — 8.8.1
 * }
 * </pre>
 */
public class CmsLogEntry extends CmsSequence {

    @CmsField
    public CmsBinaryTime timeOfEntry;
    @CmsField
    public CmsEntryId entryID;
    @CmsField(sequenceOf = true, elementType = CmsLogDataEntry.class)
    public List<CmsLogDataEntry> entryData; /* SEQUENCE OF LogDataEntry */

    public CmsLogEntry() {
        super(new InnerLogEntry());
        this.entryData = new ArrayList<>();
    }

    public CmsLogEntry timeOfEntry(CmsBinaryTime v) {
        this.timeOfEntry.value(v);
        return this;
    }
    public CmsLogEntry entryID(byte[] v) {
        this.entryID.value(v);
        return this;
    }
    public CmsLogEntry entryData(List<CmsLogDataEntry> v) {
        this.entryData = v;
        return this;
    }

    /** Copy all field values from another CmsLogEntry (fluent). */
    public CmsLogEntry value(CmsLogEntry v) {
        timeOfEntry(v.timeOfEntry);
        entryID(v.entryID.value());
        this.entryData.clear();
        for (CmsLogDataEntry e : v.entryData) {
            this.entryData.add(new CmsLogDataEntry().value(e));
        }
        return this;
    }
}
