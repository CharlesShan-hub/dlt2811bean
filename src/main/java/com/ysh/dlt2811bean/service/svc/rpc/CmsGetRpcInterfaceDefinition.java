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
 * CMS Service Code 0x70 — GetRpcInterfaceDefinition (get RPC interface definition service).
 *
 * Corresponds to Table 79 in GB/T 45906.3-2025: GetRpcInterfaceDefinition service parameters.
 *
 * Service code: 0x70 (112)
 * Service interface: GetRpcInterfaceDefinition
 * Category: Remote Procedure Call service
 *
 * The GetRpcInterfaceDefinition service is used by a client to retrieve the complete
 * definitions of all methods associated with a specified RPC interface. The client
 * provides the interface name and an optional starting point (referenceAfter) for
 * pagination. The server responds with a sequence of method definitions, including
 * name, version, timeout, and data structure definitions for request and response,
 * along with a continuation flag.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to get the RPC interface definition</li>
 *   <li>RESPONSE_POSITIVE - Server response containing method definitions and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ interface                    VisibleString                   │
 * │ referenceAfter               VisibleString (OPTIONAL)        │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ method                       SEQUENCE OF SEQUENCE {          │
 * │   name                       VisibleString                   │
 * │   version                    INT32U                          │
 * │   timeout                    INT32U                          │
 * │   request                    DataDefinition                  │
 * │   response                   DataDefinition                  │
 * │ }                                                            │
 * │ moreFollows                  BOOLEAN (DEFAULT TRUE)          │
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
 * GetRpcInterfaceDefinition-RequestPDU:: = SEQUENCE {
 *   interface          [0] IMPLICIT VisibleString,
 *   referenceAfter     [1] IMPLICIT VisibleString OPTIONAL
 * }
 *
 * GetRpcInterfaceDefinition-ResponsePDU:: = SEQUENCE {
 *   method             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     name             [0] IMPLICIT VisibleString,
 *     version          [1] IMPLICIT INT32U,
 *     timeout          [2] IMPLICIT INT32U,
 *     request          [3] IMPLICIT DataDefinition,
 *     response         [4] IMPLICIT DataDefinition
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetRpcInterfaceDefinition-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetRpcInterfaceDefinition extends CmsAsdu<CmsGetRpcInterfaceDefinition> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetRpcInterfaceDefinition(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetRpcInterfaceDefinition does not support " + messageType);
        }
    }

    public CmsGetRpcInterfaceDefinition(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_INTERFACE_DEFINITION;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetRpcInterfaceDefinition copy() {
        CmsGetRpcInterfaceDefinition copy = new CmsGetRpcInterfaceDefinition(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetRpcInterfaceDefinition read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetRpcInterfaceDefinition) new CmsGetRpcInterfaceDefinition(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetRpcInterfaceDefinition getRpcInterfaceDefinition) {
        getRpcInterfaceDefinition.encode(pos);
    }

}
