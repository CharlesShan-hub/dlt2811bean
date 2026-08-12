package com.ysh.jcms.core.data.sequence.report;

import com.ysh.jcms.data.InnerReportPDUEntry;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsEntryId;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * ReportEntry ::= SEQUENCE {
 *     timeOfEntry     [0] IMPLICIT EntryTime OPTIONAL,
 *     entryID         [1] IMPLICIT EntryID OPTIONAL,
 *     entryData       [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference OPTIONAL,
 *         fc          [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *         id          [2] IMPLICIT INT16U,
 *         value       [3] IMPLICIT Data,
 *         reason      [4] IMPLICIT ReasonCode OPTIONAL
 *     }
 * } — 8.7.1 (inline within ReportPDU)
 * }
 * </pre>
 */
public class CmsReportEntry extends CmsSequence {

    @CmsField(optional = true)
    public CmsBinaryTime timeOfEntry;
    @CmsField(optional = true)
    public CmsEntryId entryID;
    @CmsField(sequenceOf = true, elementType = CmsReportDataEntry.class)
    public List<CmsReportDataEntry> entryData;

    public CmsReportEntry() {
        super(new InnerReportPDUEntry());
        this.entryData = new ArrayList<>();
    }

    public CmsReportEntry timeOfEntry(CmsBinaryTime v) {
        if (v != null) {
            this.timeOfEntry.value(v);
            setPresent("timeOfEntry", true);
        } else {
            setPresent("timeOfEntry", false);
        }
        return this;
    }
    public CmsReportEntry entryID(byte[] v) {
        if (v != null) {
            this.entryID.value(v);
            setPresent("entryID", true);
        } else {
            setPresent("entryID", false);
        }
        return this;
    }
    public CmsReportEntry entryData(List<CmsReportDataEntry> v) {
        this.entryData = v;
        return this;
    }

    public CmsReportEntry value(CmsReportEntry v) {
        if (v.isPresent("timeOfEntry")) {
            this.timeOfEntry.value(v.timeOfEntry);
            setPresent("timeOfEntry", true);
        } else {
            setPresent("timeOfEntry", false);
        }
        if (v.isPresent("entryID")) {
            this.entryID.value(v.entryID.value());
            setPresent("entryID", true);
        } else {
            setPresent("entryID", false);
        }
        this.entryData.clear();
        for (CmsReportDataEntry e : v.entryData) {
            this.entryData.add(new CmsReportDataEntry().value(e));
        }
        return this;
    }
}
