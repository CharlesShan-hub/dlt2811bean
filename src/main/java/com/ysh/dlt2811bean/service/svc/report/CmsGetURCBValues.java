package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorUrcbChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x5D — GetURCBValues (get unbuffered report control block values).
 *
 * Corresponds to Table 49 in GB/T 45906.3-2025: GetURCBValues service parameters.
 *
 * Service code: 0x5D (93)
 * Service interface: GetURCBValues
 * Category: Reporting service
 *
 * The GetURCBValues service is used to retrieve all attributes of the unbuffered
 * report control block (URCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get URCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or URCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference[0..n]             SEQUENCE OF ObjectReference     │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ urcb[0..n]                  SEQUENCE OF CHOICE {            │
 * │   error                     [0] IMPLICIT ServiceError       │
 * │   value                     [1] IMPLICIT URCB               │
 * │ }                                                           │
 * │ moreFollows                [1] IMPLICIT BOOLEAN DEFAULT TRUE│
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * URCB:: = SEQUENCE {
 *   rptID                        [1] IMPLICIT VisibleString129,
 *   rptEna                       [2] IMPLICIT BOOLEAN,
 *   datSet                       [3] IMPLICIT ObjectReference,
 *   confRev                      [4] IMPLICIT INT32U,
 *   optFlds                      [5] IMPLICIT RCBOptFlds,
 *   bufTm                        [6] IMPLICIT INT32U,
 *   sqNum                        [7] IMPLICIT INT8U,
 *   trgOps                       [8] IMPLICIT TriggerConditions,
 *   intgPd                       [9] IMPLICIT INT32U,
 *   gi                           [10] IMPLICIT BOOLEAN,
 *   resv                         [14] IMPLICIT BOOLEAN,
 *   owner                        [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * }
 *
 * GetURCBValues-RequestPDU:: = SEQUENCE {
 *   reference                    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetURCBValues-ResponsePDU:: = SEQUENCE {
 *   urcb                         [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error                      [0] IMPLICIT ServiceError,
 *     value                      [1] IMPLICIT URCB
 *   },
 *   moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetURCBValues-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetURCBValues extends CmsAsdu<CmsGetURCBValues> {

    // ==================== Fields based on Table 49 ====================

    @CmsField(only = {REQUEST})
    public CmsArray<CmsObjectReference> reference = new CmsArray<>(CmsObjectReference::new).capacity(100);

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsArray<CmsErrorUrcbChoice> urcb = new CmsArray<>(CmsErrorUrcbChoice::new).capacity(100);
    
    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetURCBValues() {
        super(ServiceName.GET_URCB_VALUES);
    }

    public CmsGetURCBValues(MessageType messageType) {
        super(ServiceName.GET_URCB_VALUES, messageType);
    }

    public CmsGetURCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetURCBValues addReference(String ref) {
        this.reference.add(new CmsObjectReference(ref));
        return this;
    }

    public CmsGetURCBValues addUrcbChoice(CmsErrorUrcbChoice choice) {
        this.urcb.add(choice);
        return this;
    }

    public CmsGetURCBValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}
