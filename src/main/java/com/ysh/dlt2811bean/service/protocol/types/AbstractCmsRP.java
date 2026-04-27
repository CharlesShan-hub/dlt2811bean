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
 * Base class for CMS services that use the <b>Request+</b> interaction mode.
 *
 * <p>In Request+ mode, the server actively initiates a request to the client
 * (e.g., CommandTermination, TimeActivatedOperateTermination).
 * The message carries a ReqID field (2-byte unsigned integer) for matching.
 *
 * <p>Message flow: Server sends REQUEST+ → Client responds with RESPONSE_POSITIVE or RESPONSE_NEGATIVE.
 *
 * <p>Subclasses implement {@link #encodeBody(PerOutputStream)} and
 * {@link #decodeBody(PerInputStream)} for their service-specific fields.
 * The ReqID is handled automatically by this base class.
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class AbstractCmsRP extends CmsApdu {

    private final ServiceCode serviceCode;
    private MessageType messageType = MessageType.REQUEST_PLUS;
    private int reqId;

    protected AbstractCmsRP(ServiceCode serviceCode, MessageType messageType) {
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

    @Override
    protected final void encodeServiceData(PerOutputStream pos) {
        new CmsInt16U(reqId).encode(pos);
        encodeBody(pos);
    }

    @Override
    protected final void decodeServiceData(PerInputStream pis) throws Exception {
        this.reqId = new CmsInt16U().decode(pis).get();
        decodeBody(pis);
    }

    // ==================== Subclass Hooks ====================

    /**
     * Encode service-specific fields into the PER stream.
     */
    protected abstract void encodeBody(PerOutputStream pos);

    /**
     * Decode service-specific fields from the PER stream.
     */
    protected abstract void decodeBody(PerInputStream pis) throws Exception;
}
