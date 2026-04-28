package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.service.svc.association.datatypes.ServerAccessPointReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 01 — Associate (association service).
 *
 * Corresponds to Table 19 in GB/T 45906.3-2025: Associate service parameters.
 *
 * Service code: 0x01 (1)
 * Service interface: Associate
 * Category: Association service
 *
 * The Associate service is used for connection authentication between
 * client and server. The service includes optional security authentication
 * parameters for secure communication scenarios.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client association request</li>
 *   <li>RESPONSE_POSITIVE - Server positive association response</li>
 *   <li>RESPONSE_NEGATIVE - Server negative association response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serverAccessPointReference   VisibleString (SIZE(0..129))    │
 * │ authenticationParameter      OCTET STRING (OPTIONAL)         │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ associationId                OCTET STRING (SIZE(64))         │
 * │ result                       ServiceError (no-error)         │
 * │ authenticationParameter      OCTET STRING                    │
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
 * Associate-RequestPDU:: = SEQUENCE {
 *   serverAccessPointReference    [0] IMPLICIT VisibleString129 OPTIONAL,
 *   authenticationParameter       [1] IMPLICIT SEQUENCE {
 *     signatureCertificate        [0] IMPLICIT OCTET STRING,
 *     signedTime                  [1] IMPLICIT UtcTime,
 *     signedValue                 [2] IMPLICIT OCTET STRING
 *   } OPTIONAL
 * }
 *
 * Associate-ResponsePDU:: = SEQUENCE {
 *   associationId                 [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *   serviceError                  [1] IMPLICIT ServiceError,
 *   authenticationParameter       [2] IMPLICIT SEQUENCE {
 *     signaturetificate           [0] IMPLICIT OCTET STRING,
 *     signedTime                  [1] IMPLICIT UtcTime,
 *     signedValue                 [2] IMPLICIT OCTET STRING
 *   } OPTIONAL
 * }
 *
 * Associate-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAssociate extends CmsAsdu<CmsAssociate> {

    public static final int ASSOC_ID_SIZE = 64;

    // ==================== Fields based on Table 19 ====================

    // --- Request parameters ---
    // serverAccessPointReference [0..1] VisibleString129
    public ServerAccessPointReference serverAccessPointReference = new ServerAccessPointReference();

    // authenticationParameter [0..1] AuthenticationParameter (optional)
    public AuthenticationParameter authenticationParameter = new AuthenticationParameter();

    // --- Response+ parameters ---
    // associationId OCTET STRING (SIZE(64))
    public CmsOctetString associationId = new CmsOctetString().size(ASSOC_ID_SIZE);

    // result (in Response+), serviceError (in Response-)
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ==================== Constructor ====================

    public CmsAssociate(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("serverAccessPointReference");
            registerField("authenticationParameter");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("associationId");
            registerField("serviceError");
            registerField("authenticationParameter");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("Associate does not support " + messageType);
        }
    }

    public CmsAssociate(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ==================== Convenience Setters ====================

    public CmsAssociate serverAccessPointReference(String iedName, String accessPoint) {
        this.serverAccessPointReference = new ServerAccessPointReference(iedName, accessPoint);
        return this;
    }

    public CmsAssociate associationId(byte[] bytes) {
        this.associationId = new CmsOctetString(bytes).size(ASSOC_ID_SIZE);
        return this;
    }

    public CmsAssociate serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceCode getServiceCode() {
        return ServiceCode.ASSOCIATE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsAssociate copy() {
        CmsAssociate copy = new CmsAssociate(messageType());
        copy.reqId.set(reqId.get());
        copy.serverAccessPointReference = serverAccessPointReference.copy();
        copy.authenticationParameter = authenticationParameter.copy();
        copy.associationId = associationId.copy();
        copy.serviceError = serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsAssociate read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsAssociate) new CmsAssociate(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsAssociate associate) {
        associate.encode(pos);
    }
}