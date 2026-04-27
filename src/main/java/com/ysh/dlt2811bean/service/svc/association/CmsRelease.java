package com.ysh.dlt2811bean.service.svc.association;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.types.AbstractCmsRR;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 03 — Release (release association).
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * Request:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                     │
 * │ associationId   OCTET STRING (SIZE(64))        │
 * └─────────────────────────────────────────────────┘
 * 
 * Response+:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                     │
 * │ associationId   OCTET STRING (SIZE(64))        │
 * │ result           ServiceError (no-error)       │
 * └─────────────────────────────────────────────────┘
 * 
 * Response-:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                     │
 * │ serviceError     ServiceError                  │
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>Reference: GB/T 45906.3 Table 19 / corresponds to ACSE A-RELEASE
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsRelease extends AbstractCmsRR<CmsRelease> {

    private static final int ASSOC_ID_SIZE = 64;

    public CmsRelease() {
        super(ServiceCode.RELEASE, MessageType.REQUEST);
    }

    public CmsRelease(MessageType messageType) {
        super(ServiceCode.RELEASE, messageType);
    }

    // ==================== Fields based on Table 20 ====================

    // serverAccessPointReference [0..1] OCTETSTRING
    private CmsOctetString associationId = new CmsOctetString().size(ASSOC_ID_SIZE);

    // IMPLICIT ServiceError (no-error) ｜ ServiceError
    private CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ==================== Convenience Setters ====================

    public CmsRelease setAssociationId(byte[] bytes) {
        this.associationId = new CmsOctetString(bytes).size(ASSOC_ID_SIZE);
        return this;
    }

    public CmsRelease serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }

    // ==================== AbstractCmsRR Hooks ====================

    @Override
    protected void encodeRequest(PerOutputStream pos) {
        associationId.encode(pos);
    }

    @Override
    protected void decodeRequest(PerInputStream pis) throws PerDecodeException {
        try{
            associationId.decode(pis);
        }catch (Exception e){
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
        try{
            associationId.decode(pis);
            serviceError.decode(pis);
        }catch (Exception e){
            throw new PerDecodeException("CmsRelease RESPONSE_POSITIVE decode failed", e);
        }
    }

    @Override
    protected void encodeResponseNegative(PerOutputStream pos) {
        serviceError.encode(pos);
    }

    @Override
    protected void decodeResponseNegative(PerInputStream pis) throws PerDecodeException {
        try{
            serviceError.decode(pis);
        }catch (Exception e){
            throw new PerDecodeException("CmsRelease RESPONSE_NEGATIVE decode failed", e);
        }
    }

    // ==================== Static Convenience Methods ====================

    public static CmsRelease read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsRelease) new CmsRelease(messageType).decode(pis);
    }

    @Override
    public CmsApdu copy() {
        CmsRelease copy = new CmsRelease(messageType());
        copy.reqId(reqId());
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
            sb.append(", result=").append(serviceError);
        } else {
            sb.append(", serviceError=").append(serviceError);
        }

        return sb.append("}").toString();
    }
}