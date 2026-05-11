package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsErrorMethodChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x71 — GetRpcMethodDefinition (get RPC method definition service).
 *
 * Corresponds to Table 80 in GB/T 45906.3-2025: GetRpcMethodDefinition service parameters.
 *
 * Service code: 0x71 (113)
 * Service interface: GetRpcMethodDefinition
 * Category: Remote Procedure Call service
 *
 * The GetRpcMethodDefinition service is used by a client to retrieve the definitions
 * of a specific set of RPC methods. The client provides a list of method references
 * (reference[1..n]). The server responds with either an error or the full method
 * definition (including timeout, version, request/response data structures) for each
 * requested method, along with a continuation flag.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to get definitions for specific RPC methods</li>
 *   <li>RESPONSE_POSITIVE - Server response containing method definitions or errors and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                    SEQUENCE OF VisibleString       │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ error/method                 SEQUENCE OF CHOICE {            │
 * │   error                      ServiceError                    │
 * │   method                     SEQUENCE {                      │
 * │     timeout                  INT32U                          │
 * │     version                  INT32U                          │
 * │     request                  DataDefinition                  │
 * │     response                 DataDefinition                  │
 * │   }                                                          │
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
 * GetRpcMethodDefinition-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT SEQUENCE OF VisibleString
 * }
 *
 * GetRpcMethodDefinition-ResponsePDU:: = SEQUENCE {
 *   errorMethod  [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error      [0] IMPLICIT ServiceError,
 *     method     [1] IMPLICIT SEQUENCE {
 *       timeout  [0] IMPLICIT INT32U,
 *       version  [1] IMPLICIT INT32U,
 *       request  [2] IMPLICIT DataDefinition,
 *       response [3] IMPLICIT DataDefinition
 *     }
 *   },
 *   moreFollows  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetRpcMethodDefinition-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetRpcMethodDefinition extends CmsAsdu<CmsGetRpcMethodDefinition> {

    // ==================== Fields based on Table 80 ====================

    @CmsField(only = {REQUEST})
    public CmsArray<CmsVisibleString> reference = new CmsArray<>(() -> new CmsVisibleString().max(255)).capacity(100);

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsArray<CmsErrorMethodChoice> errorMethod = new CmsArray<>(CmsErrorMethodChoice::new).capacity(100);
    
    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetRpcMethodDefinition() {
        super(ServiceName.GET_RPC_METHOD_DEFINITION);
    }
    
    public CmsGetRpcMethodDefinition(MessageType messageType) {
        super(ServiceName.GET_RPC_METHOD_DEFINITION, messageType);
    }

    public CmsGetRpcMethodDefinition(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetRpcMethodDefinition addReference(String ref) {
        this.reference.add(new CmsVisibleString(ref).max(255));
        return this;
    }

    public CmsGetRpcMethodDefinition addErrorMethodChoice(CmsErrorMethodChoice choice) {
        this.errorMethod.add(choice);
        return this;
    }

    public CmsGetRpcMethodDefinition serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}
