package com.ysh.jcms.pdu.report;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerReportPDU;
import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.data.sequence.report.CmsReportEntry;

/**
 * ReportPDU ::= SEQUENCE {
 *     rptID               [0] IMPLICIT VisibleString (SIZE (0..129)),
 *     optFlds             [1] IMPLICIT RcbOptFlds,
 *     sqNum               [2] IMPLICIT Int16U OPTIONAL,
 *     subSeqNum           [3] IMPLICIT Int16U OPTIONAL,
 *     moreSegmentsFollow  [4] IMPLICIT Boolean OPTIONAL,
 *     dataSet             [5] IMPLICIT ObjectReference OPTIONAL,
 *     bufOvfl             [6] IMPLICIT Boolean OPTIONAL,
 *     confRev             [7] IMPLICIT Int32U OPTIONAL,
 *     entry               [8] IMPLICIT ReportEntry
 * } — 8.7.1 (unconfirmed service 0x35 — no Response or Error PDU)
 */
public class CmsReport extends CmsSequence {

    @CmsField public CmsString rptID;
    @CmsField public CmsRcbOptFlds optFlds;
    @CmsField(optional = true) public CmsInt16U sqNum;
    @CmsField(optional = true) public CmsInt16U subSeqNum;
    @CmsField(optional = true) public CmsBoolean moreSegmentsFollow;
    @CmsField(optional = true) public CmsObjectReference dataSet;
    @CmsField(optional = true) public CmsBoolean bufOvfl;
    @CmsField(optional = true) public CmsInt32U confRev;
    @CmsField public CmsReportEntry entry;

    public CmsReport() {
        super(new InnerReportPDU());
    }

    public CmsReport rptID(String v) { this.rptID.value(v); return this; }
    public CmsReport rptID(byte[] v) { return rptID(new String(v, StandardCharsets.UTF_8)); }
    public CmsReport optFlds(CmsRcbOptFlds v) { this.optFlds.value(v); return this; }
    public CmsReport sqNum(int v) { this.sqNum.value(v); setPresent("sqNum", true); return this; }
    public CmsReport subSeqNum(int v) { this.subSeqNum.value(v); setPresent("subSeqNum", true); return this; }
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
    public CmsReport dataSet(byte[] v) { return dataSet(v != null ? new String(v, StandardCharsets.UTF_8) : null); }
    public CmsReport bufOvfl(boolean v) {
        this.bufOvfl.value(v);
        setPresent("bufOvfl", true);
        return this;
    }
    public CmsReport confRev(long v) { this.confRev.value(v); setPresent("confRev", true); return this; }
    public CmsReport entry(CmsReportEntry v) { this.entry.value(v); return this; }
}
