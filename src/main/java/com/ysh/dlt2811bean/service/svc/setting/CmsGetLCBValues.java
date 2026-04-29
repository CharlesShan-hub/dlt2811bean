package com.ysh.dlt2811bean.service.svc.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x5F — GetLCBValues (get log control block values).
 *
 * Corresponds to Table 52 in GB/T 45906.3-2025: GetLCBValues service parameters.
 *
 * Service code: 0x5F (95)
 * Service interface: GetLCBValues
 * Category: Logging service
 *
 * The GetLCBValues service is used to retrieve all attributes of the log
 * control block (LCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get LCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or LCB data</li>
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
 * │ lcb[0..n]                   SEQUENCE OF CHOICE {            │
 * │   error                     [0] IMPLICIT ServiceError       │
 * │   value                     [1] IMPLICIT LCB                │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN (DEFAULT TRUE)          │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ serviceError                ServiceError                    │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * LCB:: = SEQUENCE {
 *   logEna                       [1] IMPLICIT BOOLEAN,
 *   datSet                       [2] IMPLICIT ObjectReference,
 *   trgOps                       [3] IMPLICIT TriggerConditions,
 *   intgPd                       [4] IMPLICIT INT32U,
 *   logRef                       [5] IMPLICIT ObjectReference,
 *   optFlds                      [6] IMPLICIT LCBOptFlds OPTIONAL,
 *   bufTm                        [7] IMPLICIT INT32U OPTIONAL
 * }
 *
 * GetLCBValues-RequestPDU:: = SEQUENCE {
 *   reference                    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetLCBValues-ResponsePDU:: = SEQUENCE {
 *   lcb                          [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error                      [0] IMPLICIT ServiceError,
 *     value                      [1] IMPLICIT LCB
 *   },
 *   moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetLCBValues-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetLCBValues extends CmsAsdu<CmsGetLCBValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetLCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetLCBValues does not support " + messageType);
        }
    }

    public CmsGetLCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LCBVALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetLCBValues copy() {
        CmsGetLCBValues copy = new CmsGetLCBValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetLCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetLCBValues) new CmsGetLCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetLCBValues getLCBValues) {
        getLCBValues.encode(pos);
    }

}
