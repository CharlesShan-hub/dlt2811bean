package com.ysh.dlt2811bean.service.association;

import com.ysh.dlt2811bean.data.string.CmsVisibleString;
import com.ysh.dlt2811bean.data.string.CmsOctetString;
import com.ysh.dlt2811bean.data.enumerated.CmsServiceError;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.types.AbstractCmsRequestResponse;
import com.ysh.dlt2811bean.service.enums.MessageType;
import com.ysh.dlt2811bean.service.enums.ServiceCode;

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
 * │ serverAccessPointReference   VisibleString (SIZE(0..129))    │
 * │ authenticationParameter      OCTET STRING (OPTIONAL)         │
 * └──────────────────────────────────────────────────────────────┘
 * 
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ associationId                OCTET STRING (SIZE(64))         │
 * │ result                       ServiceError (no-error)         │
 * │ authenticationParameter      OCTET STRING                    │
 * └──────────────────────────────────────────────────────────────┘
 * 
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmsAssociate extends AbstractCmsRequestResponse {

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
    private CmsVisibleString serverAccessPointReference = new CmsVisibleString().max(129);

    // authenticationParameter [0..1] OCTET STRING
    private CmsOctetString authenticationParameter = new CmsOctetString().max(65535);

    // --- Response+ parameters ---
    // associationId OCTET STRING (SIZE(64))
    private CmsOctetString associationId = new CmsOctetString().size(64);

    // result ServiceError = no-error
    private CmsServiceError result = new CmsServiceError(CmsServiceError.NO_ERROR);

    // authenticationParameter OCTET STRING (Response also has this parameter)
    private CmsOctetString responseAuthenticationParameter = new CmsOctetString().max(65535);

    // --- Response- parameters ---
    // serviceError ServiceError
    private CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ==================== Encode ====================

    /**
     * Encodes the service body to the specified PER output stream.
     * The encoding format depends on the message type.
     *
     * @param pos the PER output stream to write to
     */
    @Override
    protected void encodeBody(PerOutputStream pos) {
        MessageType type = getMessageType();

        if (type == MessageType.REQUEST) {
            serverAccessPointReference.encode(pos);
            authenticationParameter.encode(pos);
        } else if (type == MessageType.RESPONSE_POSITIVE) {
            associationId.encode(pos);
            result.encode(pos);
            responseAuthenticationParameter.encode(pos);
        } else if (type == MessageType.RESPONSE_NEGATIVE) {
            serviceError.encode(pos);
        }
    }

    // ==================== Decode ====================

    /**
     * Decodes the service body from the specified PER input stream.
     * The decoding format depends on the message type.
     *
     * @param pis the PER input stream to read from
     * @throws PerDecodeException if decoding fails
     */
    @Override
    protected void decodeBody(PerInputStream pis) throws PerDecodeException {
        try {
            MessageType type = getMessageType();

            if (type == MessageType.REQUEST) {
                serverAccessPointReference.decode(pis);
                authenticationParameter.decode(pis);
            } else if (type == MessageType.RESPONSE_POSITIVE) {
                associationId.decode(pis);
                result.decode(pis);
                responseAuthenticationParameter.decode(pis);
            } else if (type == MessageType.RESPONSE_NEGATIVE) {
                serviceError.decode(pis);
            }
        } catch (Exception e) {
            throw new PerDecodeException("CmsAssociate decode failed", e);
        }
    }

    // ==================== Object methods ====================

    /**
     * Returns a string representation of this CmsAssociate object.
     * The string format depends on the message type.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CmsAssociate{");
        sb.append("reqId=").append(getReqId());

        if (getMessageType() == MessageType.REQUEST) {
            sb.append(", serverAccessPointReference=").append(serverAccessPointReference);
            sb.append(", authParam=").append(authenticationParameter);
        } else if (getMessageType() == MessageType.RESPONSE_POSITIVE) {
            sb.append(", associationId=").append(associationId);
            sb.append(", result=").append(result);
            sb.append(", respAuthParam=").append(responseAuthenticationParameter);
        } else {
            sb.append(", serviceError=").append(serviceError);
        }

        return sb.append("}").toString();
    }
}