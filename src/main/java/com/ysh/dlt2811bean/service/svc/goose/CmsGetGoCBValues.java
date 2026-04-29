package com.ysh.dlt2811bean.service.svc.goose;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x66 — GetGoCBValues (get GOOSE control block values).
 *
 * Corresponds to Table 60 in GB/T 45906.3-2025: GetGoCBValues service parameters.
 *
 * Service code: 0x66 (102)
 * Service interface: GetGoCBValues
 * Category: GOOSE control block service
 *
 * The GetGoCBValues service is used to retrieve the configuration values of
 * one or more GOOSE Control Blocks (GoCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get GOOSE control block values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with GoCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ gocbReference[0..n]         SEQUENCE OF ObjectReference    │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ error/gocb[0..n]            SEQUENCE OF CHOICE {           │
 * │   error                     ServiceError                   │
 * │   gocb                      GoCB                           │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN DEFAULT TRUE           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                    │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetGoCBValues-RequestPDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetGoCBValues-ResponsePDU::= SEQUENCE {
 *   errorGocb        [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error          [0] IMPLICIT ServiceError,
 *     gocb           [1] IMPLICIT GoCB
 *   },
 *   moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetGoCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetGoCBValues extends CmsAsdu<CmsGetGoCBValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetGoCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetGoCBValues does not support " + messageType);
        }
    }

    public CmsGetGoCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_GOCBVALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetGoCBValues copy() {
        CmsGetGoCBValues copy = new CmsGetGoCBValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetGoCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetGoCBValues) new CmsGetGoCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetGoCBValues getGoCBValues) {
        getGoCBValues.encode(pos);
    }

}
