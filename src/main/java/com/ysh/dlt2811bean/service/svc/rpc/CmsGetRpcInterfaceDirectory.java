package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x6E — GetRpcInterfaceDirectory (get RPC interface directory service).
 *
 * Corresponds to Table 77 in GB/T 45906.3-2025: GetRpcInterfaceDirectory service parameters.
 *
 * Service code: 0x6E (110)
 * Service interface: GetRpcInterfaceDirectory
 * Category: Remote Procedure Call service
 *
 * The GetRpcInterfaceDirectory service is used by a client to retrieve a list of all
 * available RPC interfaces on the server. This service supports pagination through
 * the use of a reference point. The client can optionally specify a starting point
 * (referenceAfter), and the server returns a list of interface names and indicates
 * if more data is available.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to get the RPC interface directory</li>
 *   <li>RESPONSE_POSITIVE - Server response containing interface references and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ referenceAfter          [0] IMPLICIT VisibleString OPTIONAL  │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference              [0] IMPLICIT SEQUENCE OF VisibleString│
 * │ moreFollows            [1] IMPLICIT BOOLEAN DEFAULT TRUE     │
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
 * GetRpcInterfaceDirectory-RequestPDU:: = SEQUENCE {
 *   referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * }
 * GetRpcInterfaceDirectory-ResponsePDU:: = SEQUENCE {
 *   reference       [0] IMPLICIT SEQUENCE OF VisibleString,
 *   moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * GetRpcInterfaceDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetRpcInterfaceDirectory extends CmsAsdu<CmsGetRpcInterfaceDirectory> {

    // ==================== Fields based on Table 77 ====================

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsVisibleString referenceAfter = new CmsVisibleString().max(255);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsVisibleString> reference = new CmsArray<>(() -> new CmsVisibleString().max(255)).capacity(100);
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetRpcInterfaceDirectory() {
    }
    
    public CmsGetRpcInterfaceDirectory(MessageType messageType) {
        super(messageType);
    }

    public CmsGetRpcInterfaceDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetRpcInterfaceDirectory referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetRpcInterfaceDirectory addReference(String ref) {
        this.reference.add(new CmsVisibleString(ref).max(255));
        return this;
    }

    public CmsGetRpcInterfaceDirectory serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_INTERFACE_DIRECTORY;
    }
}
