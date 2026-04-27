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
public abstract class AbstractCmsRR extends CmsAsdu implements CmsService {

    private final ServiceCode serviceCode;
    private MessageType messageType = MessageType.REQUEST;
    private int reqId;

    protected AbstractCmsRR(ServiceCode serviceCode, MessageType messageType) {
        this.serviceCode = serviceCode;
        this.messageType = messageType;
    }

    // ==================== CmsService ====================

    @Override
    public ServiceCode getServiceCode() {
        return serviceCode();
    }

    // ==================== CmsAsdu Hooks ====================

    @Override
    protected final void encodeServiceData(PerOutputStream pos) {
        if (messageType != MessageType.REQUEST) {
            messageType = resolveResponseType();
        }
        new CmsInt16U(reqId).encode(pos);
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

    /**
     * Resolve an ambiguous RESPONSE type to RESPONSE_POSITIVE or RESPONSE_NEGATIVE.
     * Called automatically during {@link #encodeServiceData(PerOutputStream)} when
     * messageType is {@link MessageType#RESPONSE}.
     * <p>Subclasses with response-specific fields should override this to inspect
     * their own state (e.g., whether a serviceError field is set).
     *
     * @return RESPONSE_POSITIVE or RESPONSE_NEGATIVE
     */
    protected MessageType resolveResponseType() {
        return MessageType.RESPONSE_POSITIVE;
    }
}
