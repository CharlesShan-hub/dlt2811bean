package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsReportEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x5A — Report (report service).
 *
 * Corresponds to Table 46 in GB/T 45906.3-2025: Report service parameters.
 *
 * Service code: 0x5A (90)
 * Service interface: Report
 * Category: Reporting service
 *
 * The Report service is used by the server to actively send subscribed data to the client.
 *
 * This class supports only RESPONSE_POSITIVE message type as reports are sent by server actively:
 * <ul>
 *   <li>RESPONSE_POSITIVE - Server positive response containing report data</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ rptID                        VisibleString129               │
 * │ optFlds                      RCBOptFlds                     │
 * │ sqNum                        INT16U (OPTIONAL)              │
 * │ subSqNum                     INT16U (OPTIONAL)              │
 * │ moreSegmentsFollow           BOOLEAN (OPTIONAL)             │
 * │ datSet                       ObjectReference (OPTIONAL)     │
 * │ bufOvfl                      BOOLEAN (OPTIONAL)             │
 * │ confRev                      INT32U (OPTIONAL)              │
 * │ entry                       SEQUENCE {                      │
 * │   timeOfEntry                EntryTime (OPTIONAL)           │
 * │   entryID                    EntryID (OPTIONAL)             │
 * │   entryData[1..n]           SEQUENCE OF SEQUENCE {          │
 * │     reference                ObjectReference (OPTIONAL)     │
 * │     fc                       FunctionalConstraint (OPTIONAL)│
 * │     id                       INT16U                         │
 * │     value                    Data                           │
 * │     reason                   ReasonCode (OPTIONAL)          │
 * │   }                                                         │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Report-ResponsePDU::= SEQUENCE {
 *   rptID               [0] IMPLICIT VisibleString129,
 *   optFlds             [1] IMPLICIT RCBOptFlds,
 *   sqNum               [2] IMPLICIT INT16U OPTIONAL,
 *   subSqNum            [3] IMPLICIT INT16U OPTIONAL,
 *   moreSegmentsFollow  [4] IMPLICIT BOOLEAN OPTIONAL,
 *   datSet              [5] IMPLICIT ObjectReference OPTIONAL,
 *   bufOvfl             [6] IMPLICIT BOOLEAN OPTIONAL,
 *   confRev             [7] IMPLICIT INT32U OPTIONAL,
 *   entry               [8] IMPLICIT SEQUENCE {
 *     timeOfEntry       [0] IMPLICIT EntryTime OPTIONAL,
 *     entryID           [1] IMPLICIT EntryID OPTIONAL,
 *     entryData         [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *       reference       [0] IMPLICIT ObjectReference OPTIONAL,
 *       fc              [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *       id              [2] IMPLICIT INT16U,
 *       value           [3] IMPLICIT Data,
 *       reason          [4] IMPLICIT ReasonCode OPTIONAL
 *     }
 *   }
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsReport extends CmsAsdu<CmsReport> {

    // ==================== Fields based on Table 46 ====================

    // --- Response+ parameters ---
    public CmsVisibleString rptID = new CmsVisibleString().size(129);
    public CmsRcbOptFlds optFlds = new CmsRcbOptFlds();
    public CmsInt16U sqNum = new CmsInt16U();
    public CmsInt16U subSqNum = new CmsInt16U();
    public CmsBoolean moreSegmentsFollow = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsBoolean bufOvfl = new CmsBoolean();
    public CmsInt32U confRev = new CmsInt32U();
    public CmsReportEntry entry = new CmsReportEntry();

    // ========================= Constructor ============================

    public CmsReport(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("rptID");
            registerField("optFlds");
            registerOptionalField("sqNum");
            registerOptionalField("subSqNum");
            registerOptionalField("moreSegmentsFollow");
            registerOptionalField("datSet");
            registerOptionalField("bufOvfl");
            registerOptionalField("confRev");
            registerField("entry");
        } else {
            throw new IllegalArgumentException("Report does not support " + messageType);
        }
    }

    public CmsReport(boolean isResp, boolean isErr) {
        this(MessageType.RESPONSE_POSITIVE);
    }

    // ====================== Convenience Setters =======================

    public CmsReport rptID(String rptID) {
        this.rptID.set(rptID);
        return this;
    }

    public CmsReport sqNum(int sqNum) {
        this.sqNum.set(sqNum);
        return this;
    }

    public CmsReport subSqNum(int subSqNum) {
        this.subSqNum.set(subSqNum);
        return this;
    }

    public CmsReport moreSegmentsFollow(boolean more) {
        this.moreSegmentsFollow.set(more);
        return this;
    }

    public CmsReport datSet(String datSet) {
        this.datSet.set(datSet);
        return this;
    }

    public CmsReport bufOvfl(boolean bufOvfl) {
        this.bufOvfl.set(bufOvfl);
        return this;
    }

    public CmsReport confRev(long confRev) {
        this.confRev.set(confRev);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.REPORT;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsReport copy() {
        CmsReport copy = new CmsReport(messageType());
        copy.reqId.set(reqId.get());
        copy.rptID = this.rptID.copy();
        copy.optFlds = this.optFlds.copy();
        copy.sqNum = this.sqNum.copy();
        copy.subSqNum = this.subSqNum.copy();
        copy.moreSegmentsFollow = this.moreSegmentsFollow.copy();
        copy.datSet = this.datSet.copy();
        copy.bufOvfl = this.bufOvfl.copy();
        copy.confRev = this.confRev.copy();
        copy.entry = this.entry.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsReport read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsReport) new CmsReport(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsReport report) {
        report.encode(pos);
    }

}
