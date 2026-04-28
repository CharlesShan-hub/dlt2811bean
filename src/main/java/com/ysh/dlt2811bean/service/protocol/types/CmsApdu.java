package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.svc.AsduFactory;

/**
 * APDU (Application Protocol Data Unit) — the complete application-layer frame.
 *
 * <p>APDU consists of an APCH (4-byte header) followed by an ASDU (service payload).
 *
 * <p><b>APDU Structure:</b></p>
 * <pre>
 * ┌──────────────────────┬──────────────────────┐
 * │ APCH (4B)            │ ASDU (variable)      │
 * │ CC + SC + FL         │ ReqID + Service Data │
 * └──────────────────────┴──────────────────────┘
 * </pre>
 */
public class CmsApdu implements CmsType<CmsApdu> {

    private CmsApch apch = new CmsApch();
    private CmsAsdu<?> asdu;
    private MessageType messageType;

    // for encode
    public CmsApdu(CmsAsdu<?> asdu, MessageType messageType) {
        this.asdu = asdu;
        this.messageType = messageType;
    }

    // for decode
    public CmsApdu() {
    }

    public CmsAsdu<?> getAsdu() {
        return asdu;
    }

    @Override
    public void encode(PerOutputStream pos) {
        apch.fromMessageType(messageType);
        apch.serviceCode(asdu.getServiceCode());
        // TODO 报文分段的逻辑以后实现
        apch.next(false);

        PerOutputStream asduBuf = new PerOutputStream();
        asdu.encode(asduBuf);
        byte[] asduBytes = asduBuf.toByteArray();

        apch.frameLength(asduBytes.length);
        apch.encode(pos);
        pos.writeBytes(asduBytes);
    }

    @Override
    public CmsApdu decode(PerInputStream pis) throws Exception {
        apch.decode(pis);
        asdu = AsduFactory.create(apch.getServiceCode(), apch.isResp(), apch.isErr());
        asdu.decode(pis);
        return this;
    }

    @Override
    public CmsApdu copy() {
        CmsApdu copy = new CmsApdu();
        copy.apch = this.apch.copy();
        copy.asdu = this.asdu.copy();
        copy.messageType = this.messageType;
        return copy;
    }
}
