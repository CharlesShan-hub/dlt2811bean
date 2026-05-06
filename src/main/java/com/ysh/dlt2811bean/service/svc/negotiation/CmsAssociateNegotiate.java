package com.ysh.dlt2811bean.service.svc.negotiation;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x9A — AssociateNegotiate (association negotiation service).
 *
 * Corresponds to Table 82 in GB/T 45906.3-2025: AssociateNegotiate service parameters.
 *
 * Service code: 0x9A (154)
 * Service interface: AssociateNegotiate
 * Category: Association service
 *
 * The AssociateNegotiate service is used for negotiating service parameters
 * between the client and the server. This includes negotiating the maximum
 * frame size (APDU), the maximum ASDU size, the protocol version, and the
 * model version supported by the server.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to negotiate association parameters</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with negotiated parameters and model version</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with service error</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ apduSize                     INT16U                          │
 * │ asduSize                     INT32U                          │
 * │ protocolVersion              INT32U                          │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ apduSize                     INT16U                          │
 * │ asduSize                     INT32U                          │
 * │ protocolVersion              INT32U                          │
 * │ modelVersion                 VisibleString                   │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * AssociateNegotiate-RequestPDU:: = SEQUENCE {
 *   apduSize               [0] IMPLICIT INT16U,
 *   asduSize               [1] IMPLICIT INT32U,
 *   protocolVersion        [2] IMPLICIT INT32U
 * }
 *
 * AssociateNegotiate-ResponsePDU:: = SEQUENCE {
 *   apduSize               [0] IMPLICIT INT16U,
 *   asduSize               [1] IMPLICIT INT32U,
 *   protocolVersion        [2] IMPLICIT INT32U,
 *   modelVersion           [3] IMPLICIT VisibleString
 * }
 *
 * AssociateNegotiate-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAssociateNegotiate extends CmsAsdu<CmsAssociateNegotiate> {

    // ==================== Fields based on Table 82 ====================

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE"})
    public CmsInt16U apduSize = new CmsInt16U();

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE"})
    public CmsInt32U asduSize = new CmsInt32U();

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE"})
    public CmsInt32U protocolVersion = new CmsInt32U();

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsVisibleString modelVersion = new CmsVisibleString().max(255);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsAssociateNegotiate() {
    }

    public CmsAssociateNegotiate(MessageType messageType) {
        super(messageType);
    }

    public CmsAssociateNegotiate(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsAssociateNegotiate apduSize(int value) {
        this.apduSize.set(value);
        return this;
    }

    public CmsAssociateNegotiate asduSize(long value) {
        this.asduSize.set(value);
        return this;
    }

    public CmsAssociateNegotiate protocolVersion(long value) {
        this.protocolVersion.set(value);
        return this;
    }

    public CmsAssociateNegotiate modelVersion(String value) {
        this.modelVersion.set(value);
        return this;
    }

    public CmsAssociateNegotiate serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ASSOCIATE_NEGOTIATE;
    }
}
