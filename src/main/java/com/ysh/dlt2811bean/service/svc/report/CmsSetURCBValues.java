package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesResultEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x5E — SetURCBValues (set unbuffered report control block values).
 *
 * Corresponds to Table 50 in GB/T 45906.3-2025: SetURCBValues service parameters.
 *
 * Service code: 0x5E (94)
 * Service interface: SetURCBValues
 * Category: Reporting service
 *
 * The SetURCBValues service is used to modify one or more attributes within the
 * unbuffered report control block (URCB).
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Set URCB values request</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with detailed results</li>
 * </ul>
 * Note: Positive response (Response+) contains no additional data.
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌───────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                    │
 * │ urcb[0..n]            SEQUENCE OF SEQUENCE {                  │
 * │   reference           [0] IMPLICIT ObjectReference            │
 * │   rptID               [1] IMPLICIT VisibleString129 OPTIONAL, │
 * │   rptEna              [2] IMPLICIT BOOLEAN OPTIONAL,          │
 * │   datSet              [3] IMPLICIT ObjectReference OPTIONAL,  │
 * │   optFlds             [5] IMPLICIT RCBOptFlds OPTIONAL,       │
 * │   bufTm               [6] IMPLICIT INT32U OPTIONAL,           │
 * │   trgOps              [8] IMPLICIT TriggerConditions OPTIONAL,│
 * │   intgPd              [9] IMPLICIT INT32U OPTIONAL,           │
 * │   gi                  [10] IMPLICIT BOOLEAN OPTIONAL,         │
 * │   resv                [13] IMPLICIT BOOLEAN OPTIONAL          │
 * │ }                                                             │
 * └───────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌───────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                    │
 * │ result[0..n]               SEQUENCE OF SEQUENCE {             │
 * │   error               [0] IMPLICIT ServiceError OPTIONAL,     │
 * │   rptID               [1] IMPLICIT ServiceError OPTIONAL,     │
 * │   rptEna              [2] IMPLICIT ServiceError OPTIONAL,     │
 * │   datSet              [3] IMPLICIT ServiceError OPTIONAL,     │
 * │   optFlds             [5] IMPLICIT ServiceError OPTIONAL,     │
 * │   bufTm               [6] IMPLICIT ServiceError OPTIONAL,     │
 * │   trgOps              [8] IMPLICIT ServiceError OPTIONAL,     │
 * │   intgPd              [9] IMPLICIT ServiceError OPTIONAL,     │
 * │   gi                  [10] IMPLICIT ServiceError OPTIONAL,    │
 * │   resv                [13] IMPLICIT ServiceError OPTIONAL     │
 * │ }                                                             │
 * └───────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetURCBValues-RequestPDU:: = SEQUENCE {
 *   urcb                          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference                  [0] IMPLICIT ObjectReference,
 *     rptID                      [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna                     [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet                     [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds                    [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm                      [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps                     [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd                     [9] IMPLICIT INT32U OPTIONAL,
 *     gi                         [10] IMPLICIT BOOLEAN OPTIONAL,
 *     resv                       [13] IMPLICIT BOOLEAN OPTIONAL
 *   }
 * }
 *
 * SetURCBValues-ResponsePDU:: = NULL
 *
 * SetURCBValues-ErrorPDU:: = SEQUENCE {
 *   result                        [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     error                      [0] IMPLICIT ServiceError OPTIONAL,
 *     rptID                      [1] IMPLICIT ServiceError OPTIONAL,
 *     rptEna                     [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet                     [3] IMPLICIT ServiceError OPTIONAL,
 *     optFlds                    [5] IMPLICIT ServiceError OPTIONAL,
 *     bufTm                      [6] IMPLICIT ServiceError OPTIONAL,
 *     trgOps                     [8] IMPLICIT ServiceError OPTIONAL,
 *     intgPd                     [9] IMPLICIT ServiceError OPTIONAL,
 *     gi                         [10] IMPLICIT ServiceError OPTIONAL,
 *     resv                       [13] IMPLICIT ServiceError OPTIONAL
 *   }
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetURCBValues extends CmsAsdu<CmsSetURCBValues> {

    // ==================== Fields based on Table 50 ====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsSetURCBValuesEntry> urcb = new CmsArray<>(CmsSetURCBValuesEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsArray<CmsSetURCBValuesResultEntry> result = new CmsArray<>(CmsSetURCBValuesResultEntry::new).capacity(100);
    
    // ========================= Constructor ============================

    public CmsSetURCBValues() {
        super(ServiceName.SET_URCB_VALUES);
    }
    
    public CmsSetURCBValues(MessageType messageType) {
        super(ServiceName.SET_URCB_VALUES, messageType);
    }

    public CmsSetURCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetURCBValues addUrcb(CmsSetURCBValuesEntry entry) {
        this.urcb.add(entry);
        return this;
    }

    public CmsSetURCBValues addResult(CmsSetURCBValuesResultEntry entry) {
        this.result.add(entry);
        return this;
    }
}
