package com.ysh.dlt2811bean.service.svc.association;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerOctetString;
import com.ysh.dlt2811bean.service.protocol.types.AbstractCmsRR;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 03 — Release (release association).
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)  = 0                                 │
 * │ AssociationId   OCTET STRING (SIZE(64))         │  association identifier
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>Reference: GB/T 45906.3 Table 19 / corresponds to ACSE A-RELEASE
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsRelease extends AbstractCmsRR<CmsRelease> {

    /** Fixed association identifier length: 64 bytes */
    private static final int ASSOC_ID_SIZE = 64;

    public CmsRelease() {
        super(ServiceCode.RELEASE, MessageType.REQUEST);
    }

    public CmsRelease(MessageType messageType) {
        super(ServiceCode.RELEASE, messageType);
    }

    // ==================== Fields ====================

    private byte[] associationId;
    private int serviceError;

    // ==================== AbstractCmsRR Hooks ====================

    @Override
    protected MessageType resolveResponseType() {
        return serviceError != 0 ? MessageType.RESPONSE_NEGATIVE : MessageType.RESPONSE_POSITIVE;
    }

    @Override
    protected void encodeRequest(PerOutputStream pos) {
        PerOctetString.encodeFixedSize(pos, associationId, ASSOC_ID_SIZE);
    }

    @Override
    protected void decodeRequest(PerInputStream pis) throws PerDecodeException {
        this.associationId = PerOctetString.decodeFixedSize(pis, ASSOC_ID_SIZE);
    }

    @Override
    protected void encodeResponsePositive(PerOutputStream pos) {
        PerOctetString.encodeFixedSize(pos, associationId, ASSOC_ID_SIZE);
    }

    @Override
    protected void decodeResponsePositive(PerInputStream pis) throws PerDecodeException {
        this.associationId = PerOctetString.decodeFixedSize(pis, ASSOC_ID_SIZE);
    }

    @Override
    protected void encodeResponseNegative(PerOutputStream pos) {
        // TODO: encode serviceError
    }

    @Override
    protected void decodeResponseNegative(PerInputStream pis) throws PerDecodeException {
        // TODO: decode serviceError
    }

    // ==================== Static Convenience Methods ====================

    /**
     * Read a Release APDU from a PER input stream.
     *
     * @param pis         PER input stream
     * @param messageType the message type (REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE)
     * @return decoded Release service
     */
    public static CmsRelease read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsRelease) new CmsRelease(messageType).decode(pis);
    }

    @Override
    public CmsApdu copy() {
        CmsRelease copy = new CmsRelease();
        copy.associationId = this.associationId != null ? this.associationId.clone() : null;
        return copy;
    }

    @Override
    public String toString() {
        if (associationId == null) return "CmsRelease{associationId=null}";
        StringBuilder sb = new StringBuilder("CmsRelease{associationId=");
        for (byte b : associationId) sb.append(String.format("%02X", b & 0xFF));
        sb.append('}');
        return sb.toString();
    }
}
