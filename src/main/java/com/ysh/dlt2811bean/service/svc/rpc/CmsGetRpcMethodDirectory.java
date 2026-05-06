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
 * CMS Service Code 0x6F — GetRpcMethodDirectory (get RPC method directory service).
 *
 * Corresponds to Table 78 in GB/T 45906.3-2025: GetRpcMethodDirectory service parameters.
 *
 * Service code: 0x6F (111)
 * Service interface: GetRpcMethodDirectory
 * Category: Remote Procedure Call service
 *
 * The GetRpcMethodDirectory service is used by a client to retrieve the names of all methods
 * associated with a specified RPC interface. The client provides the interface name and an
 * optional starting point (referenceAfter) for pagination. The server responds with a list
 * of method names and indicates if more data is available.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to get the RPC method directory</li>
 *   <li>RESPONSE_POSITIVE - Server response containing method references and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ interface               [0] IMPLICIT VisibleString OPTIONAL  │
 * │ referenceAfter          [1] IMPLICIT VisibleString OPTIONAL  │
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
 * │ serviceError                ServiceError                     │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetRpcMethodDirectory-RequestPDU:: = SEQUENCE {
 *   interface            [0] IMPLICIT VisibleString OPTIONAL,
 *   referenceAfter       [1] IMPLICIT VisibleString OPTIONAL
 * }
 * GetRpcMethodDirectory-ResponsePDU:: = SEQUENCE {
 *   reference            [0] IMPLICIT SEQUENCE OF VisibleString,
 *   moreFollows          [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * GetRpcMethodDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetRpcMethodDirectory extends CmsAsdu<CmsGetRpcMethodDirectory> {

    // ==================== Fields based on Table 78 ====================

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsVisibleString interfaceName = new CmsVisibleString().max(255);

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsVisibleString referenceAfter = new CmsVisibleString().max(255);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsVisibleString> reference = new CmsArray<>(() -> new CmsVisibleString().max(255)).capacity(100);
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetRpcMethodDirectory() {
        super(ServiceName.GET_RPC_METHOD_DIRECTORY);
    }
    
    public CmsGetRpcMethodDirectory(MessageType messageType) {
        super(ServiceName.GET_RPC_METHOD_DIRECTORY, messageType);
    }

    public CmsGetRpcMethodDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetRpcMethodDirectory interfaceName(String name) {
        this.interfaceName.set(name);
        return this;
    }

    public CmsGetRpcMethodDirectory referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetRpcMethodDirectory addReference(String ref) {
        this.reference.add(new CmsVisibleString(ref).max(255));
        return this;
    }

    public CmsGetRpcMethodDirectory serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}
