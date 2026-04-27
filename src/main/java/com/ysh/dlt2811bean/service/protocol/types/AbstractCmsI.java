package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * Base class for CMS services that use the <b>Indication</b> interaction mode.
 *
 * <p>In Indication mode, the server actively sends data to the client
 * without a preceding request (e.g., Report, GOOSE, MSV).
 * These services do <b>not</b> carry a ReqID field.
 *
 * <p>Message flow: Server sends INDICATION → Client receives (no response).
 *
 * <p>Subclasses implement {@link #encodeBody(PerOutputStream)} and
 * {@link #decodeBody(PerInputStream)} for their service-specific fields.
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class AbstractCmsI extends CmsAsdu implements CmsService {

    private final ServiceCode serviceCode;
    private MessageType messageType = MessageType.INDICATION;

    protected AbstractCmsI(ServiceCode serviceCode) {
        this.serviceCode = serviceCode;
    }

    // ==================== CmsService ====================

    @Override
    public ServiceCode getServiceCode() {
        return serviceCode();
    }

    // ==================== CmsAsdu Hooks ====================

    @Override
    protected final void encodeServiceData(PerOutputStream pos) {
        encodeBody(pos);
    }

    @Override
    protected final void decodeServiceData(PerInputStream pis) throws Exception {
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
