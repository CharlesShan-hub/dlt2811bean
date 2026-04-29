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
 * 8.9.2 读 GOOSE 引用服务 (GetGoReference)
 *
 * Corresponds to Table 58 in GB/T 45906.3-2025: GetGoReference service parameters.
 *
 * Service code: N/A (Part of GOOSE Management Services)
 * Service interface: GetGoReference
 * Category: General station event service
 *
 * The GetGoReference service is used to retrieve the references and functional
 * constraints of the members within a GOOSE Control Block (GoCB) dataset.
 * This service is typically used by a client to discover the structure of a
 * GOOSE dataset published by a server.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client request for GOOSE member references</li>
 *   <li>RESPONSE_POSITIVE - Server response containing dataset structure</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ memberOffset               INT16U (1..n)                     │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ confRev                    INT32U                            │
 * │ datSet                     ObjectReference                   │
 * │ memberData                 SEQUENCE OF SEQUENCE {            │
 * │   reference                ObjectReference                   │
 * │   fc                       FunctionalConstraint              │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ serviceError               ServiceError                      │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetGoReference-RequestPDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   memberOffset      [1] IMPLICIT SEQUENCE OF INT16U
 * }
 *
 * GetGoReference-ResponsePDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   confRev          [1] IMPLICIT INT32U,
 *   datSet           [2] IMPLICIT ObjectReference,
 *   memberData       [3] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * GetGoReference-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class GetGoReference extends CmsAsdu<GetGoReference> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public GetGoReference(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetGoReference does not support " + messageType);
        }
    }

    public GetGoReference(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.Get_Go_Reference;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public GetGoReference copy() {
        GetGoReference copy = new GetGoReference(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static GetGoReference read(PerInputStream pis, MessageType messageType) throws Exception {
        return (GetGoReference) new GetGoReference(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, GetGoReference getGoReference) {
        getGoReference.encode(pos);
    }

}
