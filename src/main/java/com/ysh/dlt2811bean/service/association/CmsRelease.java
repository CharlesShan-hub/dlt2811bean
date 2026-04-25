package com.ysh.dlt2811bean.service.association;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerOctetString;
import com.ysh.dlt2811bean.service.CmsService;
import com.ysh.dlt2811bean.service.ServiceCode;

/**
 * CMS Service Code 03 — Release (release association).
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │ AssociationId   OCTET STRING (SIZE(64))         │  association identifier
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>Reference: GB/T 45906.3 Table 19 / corresponds to ACSE A-RELEASE
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmsRelease extends CmsService {

    /** Fixed association identifier length: 64 bytes */
    private static final int ASSOC_ID_SIZE = 64;

    public CmsRelease() {
        super(ServiceCode.RELEASE);
    }

    // ==================== Fields ====================

    private byte[] associationId;

    // ==================== Encode ====================

    @Override
    protected byte[] encodeAsdu() {
        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeFixedSize(pos, associationId, ASSOC_ID_SIZE);
        return pos.toByteArray();
    }

    // ==================== Decode ====================

    @Override
    protected void decodeAsdu(PerInputStream pis) throws PerDecodeException {
        this.associationId = PerOctetString.decodeFixedSize(pis, ASSOC_ID_SIZE);
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
