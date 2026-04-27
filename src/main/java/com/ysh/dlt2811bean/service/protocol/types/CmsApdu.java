package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import lombok.Getter;

/**
 * APDU (Application Protocol Data Unit) — the complete frame.
 *
 * <p>APDU = APCH (5 bytes) + ASDU (ReqID + Service Data)
 *
 * <p>Encoding order:
 * <ol>
 *   <li>Encode ASDU to temporary buffer to calculate FL</li>
 *   <li>Set APCH.FL = ASDU length</li>
 *   <li>Write APCH</li>
 *   <li>Write ASDU</li>
 * </ol>
 *
 * <p>Decoding order:
 * <ol>
 *   <li>Read APCH</li>
 *   <li>Read ASDU bytes (length = APCH.FL)</li>
 *   <li>Parse ASDU</li>
 * </ol>
 *
 * @param <T> concrete ASDU type (e.g. CmsAssociate)
 */
@Getter
public class CmsApdu<T extends CmsAsdu> implements CmsType<CmsApdu<T>> {

    private final CmsApch apch = new CmsApch();
    private final T asdu;

    public CmsApdu(T asdu) {
        this.asdu = asdu;
    }

    // ==================== Fluent Setters ====================

    public CmsApdu<T> withServiceCode(ServiceCode serviceCode) {
        apch.withServiceCode(serviceCode);
        return this;
    }

    public CmsApdu<T> withMessageType(MessageType messageType) {
        apch.withMessageType(messageType);
        return this;
    }

    public CmsApdu<T> withFragmented(boolean fragmented) {
        apch.withFragmented(fragmented);
        return this;
    }

    // ==================== CmsType ====================

    @Override
    public void encode(PerOutputStream pos) {
        PerOutputStream asduBuf = new PerOutputStream();
        asdu.encode(asduBuf);
        byte[] asduBytes = asduBuf.toByteArray();

        apch.withFrameLength(asduBytes.length);
        apch.encode(pos);
        pos.writeBytes(asduBytes);
    }

    @Override
    public CmsApdu<T> decode(PerInputStream pis) throws Exception {
        apch.decode(pis);

        int fl = apch.getFrameLength();
        byte[] asduBytes = pis.readBytes(fl);

        asdu.decode(new PerInputStream(asduBytes));

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsApdu<T> copy() {
        CmsApdu<T> copy = new CmsApdu<>((T) asdu.copy());
        copy.apch.pi.set(this.apch.pi.get());
        copy.apch.sc.set(this.apch.sc.get());
        copy.apch.flags.set(this.apch.flags.get());
        copy.apch.fl.set(this.apch.fl.get());
        return copy;
    }

    @Override
    public String toString() {
        return "CmsApdu{apch=" + apch + ", asdu=" + asdu + "}";
    }
}
