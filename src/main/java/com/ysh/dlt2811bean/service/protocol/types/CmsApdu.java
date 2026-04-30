package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.svc.AsduFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static final int MAX_ASDU_SIZE = 65531;

    private CmsApch apch = new CmsApch();
    private CmsAsdu<?> asdu;
    private MessageType messageType;
    private byte[] asduBytes;
    private boolean segmented;
    private int actualAsduSize;

    // for encode
    public CmsApdu(CmsAsdu<?> asdu) {
        this.asdu = asdu;
        this.messageType = asdu.messageType();
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
        if (actualAsduSize > MAX_ASDU_SIZE) {
            throw new IllegalStateException(
                "ASDU size " + actualAsduSize + " exceeds maximum " + MAX_ASDU_SIZE
                + ", call split() first");
        }
        apch.encode(pos);
        pos.writeBytes(asduBytes);
    }

    @Override
    public CmsApdu decode(PerInputStream pis) throws Exception {
        load(pis);
        if (apch.isNext()) {
            throw new IllegalStateException("Cannot decode segmented frame, use decode(PerInputStream, List) instead");
        }
        asdu = AsduFactory.create(apch.getServiceCode(), apch.isResp(), apch.isErr());
        asdu.decode(new PerInputStream(asduBytes));
        messageType = asdu.messageType();
        return this;
    }

    public CmsApdu decode(PerInputStream pis, List<CmsApdu> previous) throws Exception {
        load(pis);
        return merge(previous);
    }

    public CmsApdu load(PerInputStream pis) throws Exception {
        apch.decode(pis);
        int len = apch.getFrameLength();
        asduBytes = pis.readBytes(len);
        return this;
    }

    public CmsApdu merge(List<CmsApdu> previous) throws Exception {
        int totalLen = 0;
        for (CmsApdu seg : previous) {
            if (seg.asduBytes == null) {
                throw new IllegalStateException("All segments must have asduBytes loaded");
            }
            totalLen += seg.asduBytes.length;
        }
        totalLen += asduBytes.length;

        byte[] merged = new byte[totalLen];
        int offset = 0;
        for (CmsApdu seg : previous) {
            System.arraycopy(seg.asduBytes, 0, merged, offset, seg.asduBytes.length);
            offset += seg.asduBytes.length;
        }
        System.arraycopy(this.asduBytes, 0, merged, offset, this.asduBytes.length);

        this.asduBytes = merged;
        this.actualAsduSize = merged.length;
        this.apch.next(false);
        this.apch.frameLength(Math.min(merged.length, MAX_ASDU_SIZE));
        this.segmented = false;
        this.asdu = AsduFactory.create(apch.getServiceCode(), apch.isResp(), apch.isErr());
        this.asdu.decode(new PerInputStream(merged));
        return this;
    }

    public List<CmsApdu> split() {
        if (asduBytes == null) {
            throw new IllegalStateException("ASDU not encoded yet");
        }

        int totalLen = asduBytes.length;
        if (totalLen <= MAX_ASDU_SIZE) {
            return Collections.singletonList(this);
        }

        List<CmsApdu> segments = new ArrayList<>();
        int offset = 0;
        while (offset < totalLen) {
            int chunkSize = Math.min(MAX_ASDU_SIZE, totalLen - offset);
            byte[] chunk = new byte[chunkSize];
            System.arraycopy(asduBytes, offset, chunk, 0, chunkSize);

            boolean isLast = (offset + chunkSize >= totalLen);

            CmsApdu segment = new CmsApdu();
            segment.asdu = this.asdu;
            segment.messageType = this.messageType;
            segment.asduBytes = chunk;
            segment.segmented = true;
            segment.actualAsduSize = chunkSize;
            segment.apch.fromMessageType(this.messageType);
            segment.apch.serviceCode(asdu.getServiceName());
            segment.apch.next(!isLast);
            segment.apch.frameLength(chunkSize);

            segments.add(segment);
            offset += chunkSize;
        }
        return segments;
    }

    @Override
    public CmsApdu copy() {
        CmsApdu copy = new CmsApdu();
        copy.apch = this.apch.copy();
        copy.asdu = this.asdu.copy();
        copy.messageType = this.messageType;
        copy.asduBytes = this.asduBytes != null ? this.asduBytes.clone() : null;
        copy.actualAsduSize = this.actualAsduSize;
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
