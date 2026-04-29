package com.ysh.dlt2811bean.service.svc.rpc;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x72 — RpcCall (remote procedure call service).
 *
 * Corresponds to Table 81 in GB/T 45906.3-2025: RpcCall service parameters.
 *
 * Service code: 0x72 (114)
 * Service interface: RpcCall
 * Category: Remote Procedure Call service
 *
 * The RpcCall service is used by a client to request the server to execute a specified
 * remote method. The client provides the method reference and either the request data
 * (for new calls) or a call ID (for continuations or cancellations). The server responds
 * with the result data and optionally a next call ID for streaming or asynchronous
 * interactions.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to invoke a remote method</li>
 *   <li>RESPONSE_POSITIVE - Server response containing result data and optional next call ID</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ method                       VisibleString                   │
 * │ reqData/callID               CHOICE {                        │
 * │   reqData                    Data                            │
 * │   callID                     OCTET STRING                    │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ rspData                      Data                            │
 * │ nextCallID                   OCTET STRING (OPTIONAL)         │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * RpcCall-RequestPDU:: = SEQUENCE {
 *   method         [0] IMPLICIT VisibleString,
 *   reqDataCallID  [1] IMPLICIT CHOICE {
 *     reqData       [0] IMPLICIT Data,
 *     callID        [1] IMPLICIT OCTET STRING
 *   }
 * }
 *
 * RpcCall-ResponsePDU:: = SEQUENCE {
 *   rspData        [0] IMPLICIT Data,
 *   nextCallID     [1] IMPLICIT OCTET STRING OPTIONAL
 * }
 *
 * RpcCall-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsRpcCall extends CmsAsdu<CmsRpcCall> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsRpcCall(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("RpcCall does not support " + messageType);
        }
    }

    public CmsRpcCall(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.RPC_CALL;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsRpcCall copy() {
        CmsRpcCall copy = new CmsRpcCall(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsRpcCall read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsRpcCall) new CmsRpcCall(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsRpcCall rpcCall) {
        rpcCall.encode(pos);
    }

}
