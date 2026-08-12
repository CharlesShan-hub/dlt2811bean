package com.ysh.jcms.core.pdu.report;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerReportPDU;
import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.data.sequence.report.CmsReportEntry;

/**
 * <pre>
 * {@code
 * ReportPDU ::= SEQUENCE {
 *     rptID           [0] IMPLICIT VisibleString129,
 *     optFlds         [1] IMPLICIT RCBOptFlds,
 *     sqNum           [2] IMPLICIT INT16U OPTIONAL,
 *     subSeqNum       [3] IMPLICIT INT16U OPTIONAL,
 *     moreSegmentsFollow [4] IMPLICIT BOOLEAN OPTIONAL,
 *     dataSet         [5] IMPLICIT ObjectReference OPTIONAL,
 *     bufOvfl         [6] IMPLICIT BOOLEAN OPTIONAL,
 *     confRev         [7] IMPLICIT INT32U OPTIONAL,
 *     entry           [8] IMPLICIT SEQUENCE {
 *         timeOfEntry     [0] IMPLICIT EntryTime OPTIONAL,
 *         entryID         [1] IMPLICIT EntryID OPTIONAL,
 *         entryData       [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *             reference   [0] IMPLICIT ObjectReference OPTIONAL,
 *             fc          [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *             id          [2] IMPLICIT INT16U,
 *             value       [3] IMPLICIT Data,
 *             reason      [4] IMPLICIT ReasonCode OPTIONAL
 *         }
 *     }
 * } — 8.7.1 (unconfirmed service 0x35 — no Response or Error PDU)
 * }
 * </pre>
 */
public class CmsReport extends CmsSequence {

    @CmsField
    public CmsString rptID;
    @CmsField
    public CmsRcbOptFlds optFlds;
    @CmsField(optional = true)
    public CmsInt16U sqNum;
    @CmsField(optional = true)
    public CmsInt16U subSeqNum;
    @CmsField(optional = true)
    public CmsBoolean moreSegmentsFollow;
    @CmsField(optional = true)
    public CmsObjectReference dataSet;
    @CmsField(optional = true)
    public CmsBoolean bufOvfl;
    @CmsField(optional = true)
    public CmsInt32U confRev;
    @CmsField
    public CmsReportEntry entry;

    public CmsReport() {
        super(new InnerReportPDU());
    }

    public CmsReport rptID(String v) {
        this.rptID.value(v);
        return this;
    }
    public CmsReport rptID(byte[] v) {
        return rptID(new String(v, StandardCharsets.UTF_8));
    }
    public CmsReport optFlds(CmsRcbOptFlds v) {
        this.optFlds.value(v);
        return this;
    }
    public CmsReport sqNum(int v) {
        this.sqNum.value(v);
        setPresent("sqNum", true);
        return this;
    }
    public CmsReport subSeqNum(int v) {
        this.subSeqNum.value(v);
        setPresent("subSeqNum", true);
        return this;
    }
    public CmsReport moreSegmentsFollow(boolean v) {
        this.moreSegmentsFollow.value(v);
        setPresent("moreSegmentsFollow", true);
        return this;
    }
    public CmsReport dataSet(String v) {
        if (v != null) {
            this.dataSet.value(v);
            setPresent("dataSet", true);
        } else {
            setPresent("dataSet", false);
        }
        return this;
    }
    public CmsReport dataSet(byte[] v) {
        return dataSet(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsReport bufOvfl(boolean v) {
        this.bufOvfl.value(v);
        setPresent("bufOvfl", true);
        return this;
    }
    public CmsReport confRev(long v) {
        this.confRev.value(v);
        setPresent("confRev", true);
        return this;
    }
    public CmsReport entry(CmsReportEntry v) {
        this.entry.value(v);
        return this;
    }
}
