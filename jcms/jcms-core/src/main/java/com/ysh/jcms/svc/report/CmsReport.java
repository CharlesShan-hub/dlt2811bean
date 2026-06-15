package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.block.CmsRcbOptFlds;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * ReportPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     rptID               [0] IMPLICIT VisibleString129,
 *     optFlds             [1] IMPLICIT RCBOptFlds,
 *     sqNum               [2] IMPLICIT INT16U OPTIONAL,
 *     subSeqNum           [3] IMPLICIT INT16U OPTIONAL,
 *     moreSegmentsFollow  [4] IMPLICIT BOOLEAN OPTIONAL,
 *     dataSet             [5] IMPLICIT ObjectReference OPTIONAL,
 *     bufOvfl             [6] IMPLICIT BOOLEAN OPTIONAL,
 *     confRev             [7] IMPLICIT INT32U OPTIONAL,
 *     entry               [8] IMPLICIT ReportEntry
 * }  —  8.7.1
 *
 * Unconfirmed service (0x35) — no Response or Error PDU.
 */
public class CmsReport extends CmsType {

    public CmsReqId            reqId;
    public CmsUint8Array       rptID;             /* VisibleString129 */
    public CmsRcbOptFlds       optFlds;
    public CmsBoolean          sqNumPresent;
    public CmsInt16U           sqNum;             /* OPTIONAL */
    public CmsBoolean          subSeqNumPresent;
    public CmsInt16U           subSeqNum;         /* OPTIONAL */
    public CmsBoolean          moreSegmentsFollowPresent;
    public CmsBoolean          moreSegmentsFollow; /* OPTIONAL */
    public CmsBoolean          dataSetPresent;
    public CmsObjectReference  dataSet;           /* OPTIONAL */
    public CmsBoolean          bufOvflPresent;
    public CmsBoolean          bufOvfl;           /* OPTIONAL */
    public CmsBoolean          confRevPresent;
    public CmsInt32U           confRev;           /* OPTIONAL */
    public CmsReportEntry      entry;

    public CmsReport() {
        this.reqId           = new CmsReqId();
        this.rptID           = new CmsUint8Array();
        this.optFlds         = new CmsRcbOptFlds();
        this.sqNumPresent    = new CmsBoolean();
        this.sqNum           = new CmsInt16U();
        this.subSeqNumPresent = new CmsBoolean();
        this.subSeqNum       = new CmsInt16U();
        this.moreSegmentsFollowPresent = new CmsBoolean();
        this.moreSegmentsFollow        = new CmsBoolean();
        this.dataSetPresent  = new CmsBoolean();
        this.dataSet         = new CmsObjectReference();
        this.bufOvflPresent  = new CmsBoolean();
        this.bufOvfl         = new CmsBoolean();
        this.confRevPresent  = new CmsBoolean();
        this.confRev         = new CmsInt32U();
        this.entry           = new CmsReportEntry();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, rptID, optFlds,
            sqNumPresent, sqNum,
            subSeqNumPresent, subSeqNum,
            moreSegmentsFollowPresent, moreSegmentsFollow,
            dataSetPresent, dataSet,
            bufOvflPresent, bufOvfl,
            confRevPresent, confRev,
            entry);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeReport(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeReport(nativePtr, data); read(); }
}
