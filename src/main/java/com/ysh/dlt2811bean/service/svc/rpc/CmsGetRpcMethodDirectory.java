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

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetRpcMethodDirectory(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetRpcMethodDirectory does not support " + messageType);
        }
    }

    public CmsGetRpcMethodDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_METHOD_DIRECTORY;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetRpcMethodDirectory copy() {
        CmsGetRpcMethodDirectory copy = new CmsGetRpcMethodDirectory(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetRpcMethodDirectory read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetRpcMethodDirectory) new CmsGetRpcMethodDirectory(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetRpcMethodDirectory getRpcMethodDirectory) {
        getRpcMethodDirectory.encode(pos);
    }

}
