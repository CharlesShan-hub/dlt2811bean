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
 * CMS Service Code 0x3D — GetSGCBValues (get setting group control block values).
 *
 * Corresponds to Table 45 in GB/T 45906.3-2025: GetSGCBValues service parameters.
 *
 * Service code: 0x59 (89)
 * Service interface: GetSGCBValues
 * Category: Setting group service
 *
 * The GetSGCBValues service is used to retrieve all attributes of the setting group
 * control block (SGCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get SGCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or SGCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ sgcbReference[0..n]         SEQUENCE OF ObjectReference     │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ error/sgcb[0..n]            SEQUENCE OF CHOICE {            │
 * │     error                    ServiceError                   │
 * │     sgcb                    SGCB                            │
 * │ }                                                           │
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
 * GetSGCBValues-RequestPDU::= SEQUENCE {
 *   sgcbReference      [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetSGCBValues-ResponsePDU::= SEQUENCE {
 *   errorSgcb         [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error            [0] IMPLICIT ServiceError,
 *     sgcb             [1] IMPLICIT SGCB
 *   }
 * }
 *
 * GetSGCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetSGCBValues extends CmsAsdu<CmsGetSGCBValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetSGCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetSGCBValues does not support " + messageType);
        }
    }

    public CmsGetSGCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_SGCBVALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetSGCBValues copy() {
        CmsGetSGCBValues copy = new CmsGetSGCBValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetSGCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetSGCBValues) new CmsGetSGCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetSGCBValues getSGCBValues) {
        getSGCBValues.encode(pos);
    }

}
