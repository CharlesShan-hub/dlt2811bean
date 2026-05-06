package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsRpcMethodDefEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
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

    // ==================== Fields based on Table 79 ====================

    @CmsField(only = {"REQUEST"})
    public CmsVisibleString interfaceName = new CmsVisibleString().max(255);

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsVisibleString referenceAfter = new CmsVisibleString().max(255);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsRpcMethodDefEntry> method = new CmsArray<>(CmsRpcMethodDefEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetRpcInterfaceDefinition() {
        super(ServiceName.GET_RPC_INTERFACE_DEFINITION);
    }
    
    public CmsGetRpcInterfaceDefinition(MessageType messageType) {
        super(ServiceName.GET_RPC_INTERFACE_DEFINITION, messageType);
    }

    public CmsGetRpcInterfaceDefinition(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetRpcInterfaceDefinition interfaceName(String name) {
        this.interfaceName.set(name);
        return this;
    }

    public CmsGetRpcInterfaceDefinition referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetRpcInterfaceDefinition serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}
