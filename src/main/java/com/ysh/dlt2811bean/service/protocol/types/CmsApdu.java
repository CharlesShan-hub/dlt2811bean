package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * APDU (Application Protocol Data Unit) — the complete application-layer frame.
 *
 * <p>Each service class is itself an APDU: it holds its own {@link CmsApch}
 * and encodes/decodes as a complete frame (APCH + ASDU).
 *
 * <p>Usage:
 * <pre>{@code
 * // Encode
 * CmsAssociate service = new CmsAssociate(MessageType.REQUEST)
 *     .serverAccessPointReference("IED1", "AP1");
 * byte[] frame = service.encode();
 *
 * // Decode
 * CmsAssociate service = new CmsAssociate(MessageType.REQUEST).decode(frame);
 * }</pre>
 */
public abstract class CmsApdu implements CmsType<CmsApdu> {

    private final CmsApch apch = new CmsApch();
    protected final ServiceCode serviceCode;
    protected MessageType messageType;

    protected CmsApdu(ServiceCode serviceCode, MessageType messageType) {
        apch.withServiceCode(serviceCode);
        apch.withMessageType(messageType);
        this.serviceCode = serviceCode;
        this.messageType = messageType;
    }

    // ==================== Service Info ====================

    /**
     * Get the service code identifying this service type.
     */
    public ServiceCode getServiceCode() {
        return serviceCode;
    }

    /**
     * Get the message type of this service instance.
     */
    public MessageType messageType() {
        return messageType;
    }

    /**
     * Set the message type of this service instance.
     *
     * @return this instance for chaining
     */
    public CmsApdu messageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    /**
     * Get the APCH (Application Protocol Control Header).
     */
    public CmsApch apch() {
        return apch;
    }

    // ==================== APDU-level encode/decode ====================

    /**
     * Encode this service as a complete APDU frame (APCH + ASDU).
     */
    @Override
    public void encode(PerOutputStream pos) {
        MessageType mt = resolveMessageType();
        apch.withServiceCode(getServiceCode());
        apch.withMessageType(mt);

        PerOutputStream asduBuf = new PerOutputStream();
        encodeServiceData(asduBuf);
        byte[] asduBytes = asduBuf.toByteArray();

        apch.withFrameLength(asduBytes.length);
        apch.encode(pos);
        pos.writeBytes(asduBytes);
    }

    /**
     * Decode a complete APDU frame (APCH + ASDU) into this service.
     */
    @Override
    public CmsApdu decode(PerInputStream pis) throws Exception {
        apch.decode(pis);

        int fl = apch.getFrameLength();
        byte[] asduBytes = pis.readBytes(fl);

        messageType(apch.getMessageType());
        decodeServiceData(new PerInputStream(asduBytes));

        return this;
    }

    // ==================== Static Convenience Methods ====================

    /**
     * Write an APDU to a PER output stream.
     *
     * @param pos  PER output stream
     * @param apdu APDU to encode
     */
    public static void write(PerOutputStream pos, CmsApdu apdu) {
        apdu.encode(pos);
    }

    // ==================== Subclass Hooks ====================

    /**
     * Resolve the message type before encoding.
     * <p>Subclasses may override this to resolve ambiguous types
     * (e.g. {@link MessageType#RESPONSE} → {@link MessageType#RESPONSE_POSITIVE}
     * or {@link MessageType#RESPONSE_NEGATIVE}) based on their field state.
     * <p>The default implementation returns the current message type as-is.
     */
    protected MessageType resolveMessageType() {
        return messageType();
    }

    /**
     * Encode service-specific fields (the ASDU payload).
     */
    protected abstract void encodeServiceData(PerOutputStream pos);

    /**
     * Decode service-specific fields (the ASDU payload).
     */
    protected abstract void decodeServiceData(PerInputStream pis) throws Exception;
}
