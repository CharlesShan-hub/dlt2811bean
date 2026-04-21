package com.ysh.dlt2811bean.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerOctetString;

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
public class Cms03 extends CmsService {

    /** Fixed association identifier length: 64 bytes */
    private static final int ASSOC_ID_SIZE = 64;

    public Cms03() {
        super(3);
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
        if (associationId == null) return "Cms03{associationId=null}";
        StringBuilder sb = new StringBuilder("Cms03{associationId=");
        for (byte b : associationId) sb.append(String.format("%02X", b & 0xFF));
        sb.append('}');
        return sb.toString();
    }
}
