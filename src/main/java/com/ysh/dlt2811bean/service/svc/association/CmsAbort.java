package com.ysh.dlt2811bean.service.svc.association;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.service.protocol.types.AbstractCmsI;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;

/**
 * CMS Service Code 02 — Abort (abort association).
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * Request:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                     │
 * │ reason          AbortReason                    │
 * └─────────────────────────────────────────────────┘
 *
 * Indication:
 * ┌─────────────────────────────────────────────────┐
 * │ reason          AbortReason                    │
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>Reference: GB/T 45906.3 §8.2.3, Table 21
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAbort extends AbstractCmsI {

    private AbortReason reason = new AbortReason();

    public CmsAbort(MessageType messageType) {
        super(ServiceCode.ABORT, messageType);
    }

    // ==================== Convenience Setters ====================

    public CmsAbort reason(int reasonCode) {
        this.reason.set(reasonCode);
        return this;
    }

    @Override
    public CmsAbort reqId(int reqId) {
        super.reqId(reqId);
        return this;
    }

    // ==================== AbstractCmsI Hooks ====================

    @Override
    protected void encodeBody(PerOutputStream pos) {
        reason.encode(pos);
    }

    @Override
    protected void decodeBody(PerInputStream pis) throws Exception {
        reason.decode(pis);
    }

    // ==================== CmsApdu Override ====================

    /**
     * Decode a complete APDU frame.
     *
     * <p>Unlike the base class, this does <b>not</b> overwrite the message type
     * from the APCH flags, because {@link MessageType#REQUEST} and
     * {@link MessageType#INDICATION} share the same APCH flag encoding
     * (Resp=0, Err=0). The message type is determined by the caller at
     * construction time.
     */
    @Override
    public CmsApdu decode(PerInputStream pis) throws Exception {
        apch().decode(pis);

        int fl = apch().getFrameLength();
        byte[] asduBytes = pis.readBytes(fl);

        decodeServiceData(new PerInputStream(asduBytes));

        return this;
    }

    // ==================== Static Convenience Methods ====================

    public static CmsAbort read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsAbort) new CmsAbort(messageType).decode(pis);
    }

    @Override
    public CmsApdu copy() {
        CmsAbort copy = new CmsAbort(messageType());
        copy.reqId(reqId());
        copy.reason = this.reason.copy();
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CmsAbort{");

        if (messageType() == MessageType.REQUEST) {
            sb.append("reqId=").append(reqId());
            sb.append(", ");
        }

        sb.append("reason=").append(reason);
        return sb.append("}").toString();
    }
}
