package com.ysh.dlt2811bean.service.association;

import com.ysh.dlt2811bean.data.numeric.CmsInt16U;
import com.ysh.dlt2811bean.data.numeric.CmsInt8U;
import com.ysh.dlt2811bean.data.string.CmsOctetString;
import com.ysh.dlt2811bean.data.string.CmsVisibleString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.CmsService;
import com.ysh.dlt2811bean.service.ServiceCode;

/**
 * CMS Service Code 01 — Associate (association request).
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID           OCTET STRING (SIZE(2))          │  2-byte request sequence number
 * │ ProtocolVersion INTEGER (0..255)                │  protocol version
 * │ ApduSize        INTEGER (0..65535)              │  local max APDU length
 * │ AsduSize        INTEGER (0..65535)              │  local max ASDU length
 * │ ServerName      VisibleString (SIZE(0..255))    │  local server name
 * └─────────────────────────────────────────────────┘
 *
 * <p>WARNING: Field definitions and constraints are placeholder values,
 * must be verified against GB/T 45906.3 standard.
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmsAssociate extends CmsService {

    public CmsAssociate() {
        super(ServiceCode.ASSOCIATE);
    }

    // ==================== Fields ====================

    private CmsOctetString reqId = new CmsOctetString().size(2);
    private CmsInt8U protocolVersion = new CmsInt8U();
    private CmsInt16U apduSize = new CmsInt16U();
    private CmsInt16U asduSize = new CmsInt16U();
    private CmsVisibleString serverName = new CmsVisibleString().max(255);

    // ==================== Encode ====================

    @Override
    protected byte[] encodeAsdu() {
        PerOutputStream pos = new PerOutputStream();

        reqId.encode(pos);
        protocolVersion.encode(pos);
        apduSize.encode(pos);
        asduSize.encode(pos);
        serverName.encode(pos);

        return pos.toByteArray();
    }

    // ==================== Decode ====================

    @Override
    protected void decodeAsdu(PerInputStream pis) throws PerDecodeException {
        try {
            reqId.decode(pis);
            protocolVersion.decode(pis);
            apduSize.decode(pis);
            asduSize.decode(pis);
            serverName.decode(pis);
        } catch (Exception e) {
            throw new PerDecodeException("Cms01 decode failed", e);
        }
    }

    @Override
    public String toString() {
        return "CmsAssociate{reqId=" + reqId
            + ", protocolVersion=" + protocolVersion
            + ", apduSize=" + apduSize
            + ", asduSize=" + asduSize
            + ", serverName=" + serverName + "}";
    }
}
