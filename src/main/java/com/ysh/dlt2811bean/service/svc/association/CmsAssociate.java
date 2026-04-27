package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.service.svc.association.datatypes.ServerAccessPointReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.types.AbstractCmsRR;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 01 — Associate (association service).
 * 
 * <p>Corresponds to Table 19 in GB/T 45906.3-2025: Associate service parameters.
 * 
 * <p>Service code: 0x01 (1)
 * Service interface: Associate
 * Category: Association service
 * 
 * <p>The Associate service is used for connection authentication between
 * client and server. The service includes optional security authentication
 * parameters for secure communication scenarios.
 * 
 * <p>This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client association request</li>
 *   <li>RESPONSE_POSITIVE - Server positive association response</li>
 *   <li>RESPONSE_NEGATIVE - Server negative association response</li>
 * </ul>
 * 
 * <p>ASDU field layout (PER encoded, in order):
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
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAssociate extends AbstractCmsRR {

    /**
     * Constructs a CmsAssociate message with the specified message type.
     *
     * @param messageType the type of message (REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE)
     */
    public CmsAssociate(MessageType messageType) {
        super(ServiceCode.ASSOCIATE, messageType);
    }

    // ==================== Fields based on Table 19 ====================

    // --- Request parameters ---
    // serverAccessPointReference [0..1] VisibleString129
    private ServerAccessPointReference serverAccessPointReference = new ServerAccessPointReference();

    // authenticationParameter [0..1] AuthenticationParameter (optional)
    private AuthenticationParameter authenticationParameter = new AuthenticationParameter();

    // --- Response+ parameters ---
    // associationId OCTET STRING (SIZE(64))
    private CmsOctetString associationId = new CmsOctetString().size(64);

    // result ServiceError = no-error
    private CmsServiceError result = new CmsServiceError(CmsServiceError.NO_ERROR);

    // --- Response- parameters ---
    // serviceError ServiceError
    private CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ==================== Convenience Setters ====================

    /**
     * Convenience method: set serverAccessPointReference from IED name and access point.
     *
     * @param iedName the IED name
     * @param accessPoint the access point name
     * @return this
     */
    public CmsAssociate serverAccessPointReference(String iedName, String accessPoint) {
        this.serverAccessPointReference = new ServerAccessPointReference(iedName, accessPoint);
        return this;
    }

    /**
     * Convenience method: set associationId from raw bytes.
     *
     * @param bytes the 64-byte association identifier
     * @return this
     */
    public CmsAssociate associationId(byte[] bytes) {
        this.associationId = new CmsOctetString(bytes).size(64);
        return this;
    }

    // ==================== AbstractCmsRR Hooks ====================

    @Override
    protected void encodeRequest(PerOutputStream pos) {
        serverAccessPointReference.encode(pos);
        authenticationParameter.encode(pos);
    }

    @Override
    protected void decodeRequest(PerInputStream pis) throws PerDecodeException {
        try {
            serverAccessPointReference.decode(pis);
            authenticationParameter.decode(pis);
        } catch (Exception e) {
            throw new PerDecodeException("CmsAssociate REQUEST decode failed", e);
        }
    }

    @Override
    protected void encodeResponsePositive(PerOutputStream pos) {
        associationId.encode(pos);
        result.encode(pos);
        authenticationParameter.encode(pos);
    }

    @Override
    protected void decodeResponsePositive(PerInputStream pis) throws PerDecodeException {
        try {
            associationId.decode(pis);
            result.decode(pis);
            authenticationParameter.decode(pis);
        } catch (Exception e) {
            throw new PerDecodeException("CmsAssociate RESPONSE_POSITIVE decode failed", e);
        }
    }

    @Override
    protected void encodeResponseNegative(PerOutputStream pos) {
        serviceError.encode(pos);
    }

    @Override
    protected void decodeResponseNegative(PerInputStream pis) throws PerDecodeException {
        try {
            serviceError.decode(pis);
        } catch (Exception e) {
            throw new PerDecodeException("CmsAssociate RESPONSE_NEGATIVE decode failed", e);
        }
    }

    @Override
    protected MessageType resolveResponseType() {
        return serviceError.get() != CmsServiceError.NO_ERROR
            ? MessageType.RESPONSE_NEGATIVE
            : MessageType.RESPONSE_POSITIVE;
    }

    @Override
    public CmsAsdu copy() {
        CmsAssociate copy = new CmsAssociate(messageType());
        copy.reqId(reqId());
        copy.serverAccessPointReference = this.serverAccessPointReference.copy();
        copy.authenticationParameter = this.authenticationParameter.copy();
        copy.associationId = this.associationId.copy();
        copy.result = this.result.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    // ==================== Object methods ====================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CmsAssociate{");
        sb.append("reqId=").append(reqId());

        if (messageType() == MessageType.REQUEST) {
            sb.append(", serverAccessPointReference=").append(serverAccessPointReference);
            sb.append(", authParam=").append(authenticationParameter);
        } else if (messageType() == MessageType.RESPONSE_POSITIVE) {
            sb.append(", associationId=").append(associationId);
            sb.append(", result=").append(result);
            sb.append(", authParam=").append(authenticationParameter);
        } else {
            sb.append(", serviceError=").append(serviceError);
        }

        return sb.append("}").toString();
    }
}
