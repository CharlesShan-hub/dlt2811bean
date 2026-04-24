package com.ysh.dlt2811bean.service;

import com.ysh.dlt2811bean.utils.per.data.CmsVisibleString;

import static com.ysh.dlt2811bean.utils.per.data.AbstractCmsString.Mode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import com.ysh.dlt2811bean.utils.per.types.PerOctetString;

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
 * </pre>
 *
 * <p>WARNING: Field definitions and constraints are placeholder values,
 * must be verified against GB/T 45906.3 standard.
 */
@Getter
@Setter
@Accessors(chain = true)
public class Cms01 extends CmsService {

    public Cms01() {
        super(1);
    }

    // ==================== Fields ====================

    private int reqId;
    private int protocolVersion;
    private int apduSize;
    private int asduSize;
    private String serverName;

    // ==================== Encode ====================

    @Override
    protected byte[] encodeAsdu() {
        PerOutputStream pos = new PerOutputStream();

        PerOctetString.encodeInt2(pos, reqId);
        PerInteger.encode(pos, protocolVersion & 0xFFL, 0, 255);
        PerInteger.encode(pos, apduSize & 0xFFFFL, 0, 65535);
        PerInteger.encode(pos, asduSize & 0xFFFFL, 0, 65535);
        CmsVisibleString.write(pos, serverName != null ? serverName : "", Mode.VARIABLE, 255);

        return pos.toByteArray();
    }

    // ==================== Decode ====================

    @Override
    protected void decodeAsdu(PerInputStream pis) throws PerDecodeException {
        this.reqId = PerOctetString.decodeInt2(pis);
        this.protocolVersion = (int) PerInteger.decode(pis, 0, 255);
        this.apduSize = (int) PerInteger.decode(pis, 0, 65535);
        this.asduSize = (int) PerInteger.decode(pis, 0, 65535);
        this.serverName = CmsVisibleString.read(pis, Mode.VARIABLE, 255).get();
    }

    @Override
    public String toString() {
        return "Cms01{reqId=" + reqId
            + ", protocolVersion=" + protocolVersion
            + ", apduSize=" + apduSize
            + ", asduSize=" + asduSize
            + ", serverName='" + serverName + "'}";
    }
}
