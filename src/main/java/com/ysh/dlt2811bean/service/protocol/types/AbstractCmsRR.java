package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * Base class for CMS services that use the <b>Request-Response</b> interaction mode.
 *
 * <p>Per section 8.1.3, request-response services carry a ReqID field
 * (2-byte unsigned integer) for matching requests to responses.
 *
 * <p>Subclasses implement the three pairs of encode/decode hooks for each
 * message type ({@link #encodeRequest}, {@link #encodeResponsePositive},
 * {@link #encodeResponseNegative} and their decode counterparts).
 * The ReqID and message type dispatch are handled automatically by this base class.
 *
 * <p>Message flow: Client sends REQUEST → Server responds with RESPONSE_POSITIVE or RESPONSE_NEGATIVE.
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class AbstractCmsRR<T extends AbstractCmsRR<T>> extends CmsApdu {

    private final ServiceCode serviceCode;
    private MessageType messageType = MessageType.REQUEST;
    private int reqId;

    @SuppressWarnings("unchecked")
    public T reqId(int reqId) {
        this.reqId = reqId;
        return (T) this;
    }

    protected AbstractCmsRR(ServiceCode serviceCode, MessageType messageType) {
        super(serviceCode, messageType);
        this.serviceCode = serviceCode;
        this.messageType = messageType;
    }

    // ==================== CmsApdu ====================

    @Override
    public ServiceCode getServiceCode() {
        return serviceCode();
    }

    // ==================== CmsApdu Hooks ====================
    
    /**
     * Resolve an ambiguous RESPONSE type to RESPONSE_POSITIVE or RESPONSE_NEGATIVE.
     * <p>Default implementation checks the {@link #serviceError} field:
     * non-zero means NEGATIVE, zero means POSITIVE.
     */
    protected MessageType resolveResponseType() {
        if (messageType == MessageType.RESPONSE) {
            messageType = resolveResponseType();
        }
        return messageType;
    }

    @Override
    protected final void encodeServiceData(PerOutputStream pos) {
        new CmsInt16U(reqId).encode(pos);
        if (messageType == MessageType.REQUEST) {
            messageType = resolveMessageType();
        }
        messageType = resolveMessageType();
        switch (messageType) {
            case REQUEST:
                encodeRequest(pos);
                break;
            case RESPONSE_POSITIVE:
                encodeResponsePositive(pos);
                break;
            case RESPONSE_NEGATIVE:
                encodeResponseNegative(pos);
                break;
            default:
                throw new IllegalStateException("Unexpected message type: " + messageType);
        }
    }

    @Override
    protected final void decodeServiceData(PerInputStream pis) throws Exception {
        this.reqId = new CmsInt16U().decode(pis).get();
        switch (messageType) {
            case REQUEST:
                decodeRequest(pis);
                break;
            case RESPONSE_POSITIVE:
                decodeResponsePositive(pis);
                break;
            case RESPONSE_NEGATIVE:
                decodeResponseNegative(pis);
                break;
            default:
                throw new IllegalStateException("Unexpected message type: " + messageType);
        }
    }

    // ==================== Subclass Hooks ====================

    /**
     * Encode REQUEST-specific fields into the PER stream.
     */
    protected abstract void encodeRequest(PerOutputStream pos);

    /**
     * Decode REQUEST-specific fields from the PER stream.
     */
    protected abstract void decodeRequest(PerInputStream pis) throws PerDecodeException;

    /**
     * Encode RESPONSE_POSITIVE-specific fields into the PER stream.
     */
    protected abstract void encodeResponsePositive(PerOutputStream pos);

    /**
     * Decode RESPONSE_POSITIVE-specific fields from the PER stream.
     */
    protected abstract void decodeResponsePositive(PerInputStream pis) throws PerDecodeException;

    /**
     * Encode RESPONSE_NEGATIVE-specific fields into the PER stream.
     */
    protected abstract void encodeResponseNegative(PerOutputStream pos);

    /**
     * Decode RESPONSE_NEGATIVE-specific fields from the PER stream.
     */
    protected abstract void decodeResponseNegative(PerInputStream pis) throws PerDecodeException;

}
