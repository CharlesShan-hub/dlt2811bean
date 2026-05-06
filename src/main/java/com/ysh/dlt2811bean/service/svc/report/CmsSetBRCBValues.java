package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x5C — SetBRCBValues (set buffered report control block values).
 *
 * Corresponds to Table 48 in GB/T 45906.3-2025: SetBRCBValues service parameters.
 *
 * Service code: 0x5C (92)
 * Service interface: SetBRCBValues
 * Category: Reporting service
 *
 * The SetBRCBValues service is used to modify one or more attributes within the
 * buffered report control block (BRCB).
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Set BRCB values request</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with detailed results</li>
 * </ul>
 * Note: Positive response (Response+) contains no additional data.
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ brcb[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   reference                ObjectReference                  │
 * │   rptID                    VisibleString129 (OPTIONAL)      │
 * │   rptEna                   BOOLEAN (OPTIONAL)               │
 * │   datSet                   ObjectReference (OPTIONAL)       │
 * │   optFlds                  RCBOptFlds (OPTIONAL)            │
 * │   bufTm                    INT32U (OPTIONAL)                │
 * │   trgOps                   TriggerConditions (OPTIONAL)     │
 * │   intgPd                   INT32U (OPTIONAL)                │
 * │   gi                       BOOLEAN (OPTIONAL)               │
 * │   purgeBuf                 BOOLEAN (OPTIONAL)               │
 * │   entryID                  EntryID (OPTIONAL)               │
 * │   resvTms                  INT16 (OPTIONAL)                 │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ result[0..n]               SEQUENCE OF ServiceError         │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetBRCBValues-RequestPDU::= SEQUENCE {
 *   brcb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference      [0] IMPLICIT ObjectReference,
 *     rptID          [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna         [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet         [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds        [4] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm          [5] IMPLICIT INT32U OPTIONAL,
 *     trgOps         [6] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd         [7] IMPLICIT INT32U OPTIONAL,
 *     gi             [8] IMPLICIT BOOLEAN OPTIONAL,
 *     purgeBuf       [9] IMPLICIT BOOLEAN OPTIONAL,
 *     entryID        [10] IMPLICIT EntryID OPTIONAL,
 *     resvTms        [11] IMPLICIT INT16 OPTIONAL
 *   }
 * }
 *
 * SetBRCBValues-ResponsePDU::= SEQUENCE {
 *   -- NULL
 * }
 *
 * SetBRCBValues-ErrorPDU::= SEQUENCE {
 *   result           [0] IMPLICIT SEQUENCE OF ServiceError
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetBRCBValues extends CmsAsdu<CmsSetBRCBValues> {

    // ==================== Fields based on Table 48 ====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsSetBRCBValuesEntry> brcb = new CmsArray<>(CmsSetBRCBValuesEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsArray<CmsServiceError> result = new CmsArray<>(CmsServiceError::new).capacity(100);

    // ========================= Constructor ============================

    public CmsSetBRCBValues() {
        super(ServiceName.SET_BRCBVALUES);
    }
    
    public CmsSetBRCBValues(MessageType messageType) {
        super(ServiceName.SET_BRCBVALUES, messageType);
    }

    public CmsSetBRCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetBRCBValues addBrcb(CmsSetBRCBValuesEntry entry) {
        this.brcb.add(entry);
        return this;
    }

    public CmsSetBRCBValues addResult(int errorCode) {
        this.result.add(new CmsServiceError(errorCode));
        return this;
    }
}
