package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 03 — Release (release association).
 *
 * <p>Corresponds to Table 20 in GB/T 45906.3-2025: Release service parameters.
 *
 * <p>Service code: 0x03 (3)
 * Service interface: Release
 * Category: Association service
 *
 * <p>The Release service is used to gracefully release an association.
 *
 * <p>This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client release request</li>
 *   <li>RESPONSE_POSITIVE - Server positive release response</li>
 *   <li>RESPONSE_NEGATIVE - Server negative release response</li>
 * </ul>
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                      │
 * │ associationId   OCTET STRING (SIZE(64))         │
 * └─────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                      │
 * │ associationId   OCTET STRING (SIZE(64))         │
 * │ result           ServiceError (no-error)        │
 * └─────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                      │
 * │ serviceError     ServiceError                   │
 * └─────────────────────────────────────────────────┘
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsRelease extends CmsAsdu<CmsRelease> {

    public static final int ASSOC_ID_SIZE = 64;

    public CmsRelease(MessageType messageType) {
        super(messageType);
    }

    public CmsRelease(boolean isResp, boolean isErr) {
        super(fromFlags(isResp, isErr));
    }

    private static MessageType fromFlags(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST;
        if (resp && !err) return MessageType.RESPONSE_POSITIVE;
        if (resp) return MessageType.RESPONSE_NEGATIVE;
        throw new IllegalArgumentException("RR mode does not support !resp && err");
    }

    // ==================== Fields based on Table 20 ====================

    private CmsOctetString associationId = new CmsOctetString().size(ASSOC_ID_SIZE);

    private CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ==================== Convenience Setters ====================

    public CmsRelease associationId(byte[] bytes) {
        this.associationId = new CmsOctetString(bytes).size(ASSOC_ID_SIZE);
        return this;
    }

    public CmsRelease serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }

    // ==================== CmsAsdu Hooks ====================

    @Override
    protected void encodeRequest(PerOutputStream pos) {
        associationId.encode(pos);
    }

    @Override
    protected void decodeRequest(PerInputStream pis) throws PerDecodeException {
        try {
            associationId.decode(pis);
        } catch (Exception e) {
            throw new PerDecodeException("CmsRelease REQUEST decode failed", e);
        }
    }

    @Override
    protected void encodeResponsePositive(PerOutputStream pos) {
        associationId.encode(pos);
        new CmsServiceError(CmsServiceError.NO_ERROR).encode(pos);
    }

    @Override
    protected void decodeResponsePositive(PerInputStream pis) throws PerDecodeException {
        try {
            associationId.decode(pis);
            serviceError.decode(pis);
        } catch (Exception e) {
            throw new PerDecodeException("CmsRelease RESPONSE_POSITIVE decode failed", e);
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
            throw new PerDecodeException("CmsRelease RESPONSE_NEGATIVE decode failed", e);
        }
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceCode getServiceCode() {
        return ServiceCode.RELEASE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    @SuppressWarnings("unchecked")
    public CmsAsdu<?> copy() {
        CmsRelease copy = new CmsRelease(messageType());
        copy.reqId().set(reqId().get());
        copy.associationId = this.associationId.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CmsRelease{");
        sb.append("reqId=").append(reqId());

        if (messageType() == MessageType.REQUEST) {
            sb.append(", associationId=").append(associationId);
        } else if (messageType() == MessageType.RESPONSE_POSITIVE) {
            sb.append(", associationId=").append(associationId);
            sb.append(", serviceError=").append(serviceError);
        } else {
            sb.append(", serviceError=").append(serviceError);
        }

        return sb.append("}").toString();
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsRelease read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsRelease) new CmsRelease(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsRelease release) {
        release.encode(pos);
    }
}