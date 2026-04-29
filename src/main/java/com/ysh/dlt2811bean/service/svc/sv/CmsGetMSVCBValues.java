package com.ysh.dlt2811bean.service.svc.sv;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x69 — GetMSVCBValues (get multicast sampling value control block values).
 *
 * Corresponds to Table 63 in GB/T 45906.3-2025: GetMSVCBValues service parameters.
 *
 * Service code: 0x69 (105)
 * Service interface: GetMSVCBValues
 * Category: MSV control block service
 *
 * The GetMSVCBValues service is used to retrieve the configuration values of
 * one or more Multicast Sampling Value Control Blocks (MSVCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get MSVCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with MSVCB data</li>
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
 * │ error/msvcb[0..n]           SEQUENCE OF CHOICE {            │
 * │   error                     ServiceError                    │
 * │   msvcb                     MSVCB                           │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN DEFAULT TRUE            │
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
 * GetMSVCBValues-RequestPDU::= SEQUENCE {
 *   reference      [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetMSVCBValues-ResponsePDU::= SEQUENCE {
 *   errorMsvcb     [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error        [0] IMPLICIT ServiceError,
 *     msvcb        [1] IMPLICIT MSVCB
 *   },
 *   moreFollows    [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetMSVCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetMSVCBValues extends CmsAsdu<CmsGetMSVCBValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetMSVCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetMSVCBValues does not support " + messageType);
        }
    }

    public CmsGetMSVCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_MSVCBVALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetMSVCBValues copy() {
        CmsGetMSVCBValues copy = new CmsGetMSVCBValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetMSVCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetMSVCBValues) new CmsGetMSVCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetMSVCBValues getMSVCBValues) {
        getMSVCBValues.encode(pos);
    }

}
