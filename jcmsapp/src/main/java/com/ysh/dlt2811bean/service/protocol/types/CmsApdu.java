package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.svc.AsduFactory;

/**
 * APDU (Application Protocol Data Unit) — the complete application-layer frame.
 */
public class CmsApdu implements CmsType<CmsApdu> {

    public static final int MAX_ASDU_SIZE = 65531;

    public CmsApch apch = new CmsApch();
    public CmsAsdu<?> asdu;
    public MessageType messageType;
    public byte[] asduBytes;
    public boolean segmented;
    public int actualAsduSize;
    public int reqId;

    // for encode
    public CmsApdu(CmsAsdu<?> asdu) {
        this.asdu = asdu;
        this.messageType = asdu.messageType();
        this.reqId = asdu.reqId().get();
        if (this.messageType == MessageType.UNKNOWN) {
            throw new IllegalArgumentException("Miss MessageType");
        }
        computeFrameLength();
    }

    // for decode
    public CmsApdu() {
    }

    public CmsAsdu<?> getAsdu() {
        return asdu;
    }

    public CmsApch getApch(){
        return apch;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public int getReqId() {
        return reqId;
    }

    private void computeFrameLength() {
        apch.fromMessageType(messageType);
        apch.serviceCode(asdu.getServiceName());
        apch.next(false);
        if (asduBytes == null) {
            PerOutputStream asduBuf = new PerOutputStream();
            asdu.encode(asduBuf);
            asduBytes = asduBuf.toByteArray();
        }
        actualAsduSize = asduBytes.length;
        apch.frameLength(Math.min(asduBytes.length, MAX_ASDU_SIZE));
    }

    @Override
    public void encode(PerOutputStream pos) {
        apch.encode(pos);
        pos.writeBytes(asduBytes);
    }

    @Override
    public CmsApdu decode(PerInputStream pis) throws Exception {
        load(pis);
        if (apch.isNext()) {
            throw new IllegalStateException("Cannot decode segmented frame, use CmsFrameManager instead");
        }
        asdu = AsduFactory.create(apch.getServiceCode(), apch.isResp(), apch.isErr());
        asdu.decode(new PerInputStream(asduBytes));
        messageType = asdu.messageType();
        reqId = asdu.reqId().get();
        return this;
    }

    /**
     * Decodes only the ASDU from already-loaded bytes (no APCH).
     * Used after {@link #load(PerInputStream)} when APCH has already been consumed.
     *
     * <p>Requires {@link #asduBytes} to be populated (done by {@link #load}).
     */
    public CmsApdu decodeAsdu() throws Exception {
        if (asduBytes == null) {
            throw new IllegalStateException("asduBytes not loaded");
        }
        asdu = AsduFactory.create(apch.getServiceCode(), apch.isResp(), apch.isErr());
        asdu.decode(new PerInputStream(asduBytes));
        messageType = asdu.messageType();
        reqId = asdu.reqId().get();
        return this;
    }

    public CmsApdu load(PerInputStream pis) throws Exception {
        apch.decode(pis);
        int len = apch.getFrameLength();
        asduBytes = pis.readBytes(len);
        if (asduBytes.length >= 2) {
            reqId = ((asduBytes[0] & 0xFF) << 8) | (asduBytes[1] & 0xFF);
        }
        return this;
    }

    @Override
    public CmsApdu copy() {
        CmsApdu copy = new CmsApdu();
        copy.apch = this.apch.copy();
        copy.asdu = this.asdu.copy();
        copy.messageType = this.messageType;
        copy.asduBytes = this.asduBytes != null ? this.asduBytes.clone() : null;
        copy.actualAsduSize = this.actualAsduSize;
        copy.reqId = this.reqId;
        return copy;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        String indent = "    ".repeat(depth + 1);
        String bracketIndent = "    ".repeat(depth);
        StringBuilder sb = new StringBuilder("(")
            .append(getClass().getSimpleName())
            .append(") {\n");

        sb.append(indent).append("messageType: ").append(messageType).append(",\n");
        sb.append(indent).append("reqId: ").append(reqId).append(",\n");

        sb.append(indent).append("apch: ");
        sb.append(apch instanceof AbstractCmsCompound
            ? ((AbstractCmsCompound<?>) apch).toString(depth + 1)
            : apch.toString());
        sb.append(",\n");

        sb.append(indent).append("asdu: ");
        if (segmented) {
            sb.append("<segmented frame, incomplete>");
        } else {
            sb.append(asdu instanceof AbstractCmsCompound
                ? ((AbstractCmsCompound<?>) asdu).toString(depth + 1)
                : asdu.toString());
        }
        sb.append("\n");

        sb.append(bracketIndent).append("}");
        return sb.toString();
    }
}
