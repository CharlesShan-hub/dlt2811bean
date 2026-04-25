package com.ysh.dlt2811bean.service.types;

import com.ysh.dlt2811bean.data.string.CmsOctetString;
import lombok.Getter;
import lombok.Setter;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.enums.MessageType;
import com.ysh.dlt2811bean.service.enums.ServiceCode;

/**
 * Base class for CMS services that use the Request-Response interaction mode.
 *
 * <p>Per section 8.1.3, request-response services carry a ReqID field
 * (2-byte OCTET STRING) for matching requests to responses.
 *
 * <p>Subclasses implement {@link #encodeBody(PerOutputStream)} and
 * {@link #decodeBody(PerInputStream)} for their service-specific fields.
 * The ReqID is handled automatically by this base class.
 */
@Getter
@Setter
public abstract class AbstractCmsRequestResponse extends CmsService {

    private CmsOctetString reqId = new CmsOctetString().size(2);

    public void setReqId(byte[] value) {
        reqId.set(value);
    }

    protected AbstractCmsRequestResponse(ServiceCode serviceCode, MessageType messageType) {
        super(serviceCode);
        setMessageType(messageType);
    }

    @Override
    protected final byte[] encodeAsdu() {
        PerOutputStream pos = new PerOutputStream();
        reqId.encode(pos);
        encodeBody(pos);
        return pos.toByteArray();
    }

    @Override
    protected final void decodeAsdu(PerInputStream pis) throws PerDecodeException {
        try {
            reqId.decode(pis);
            decodeBody(pis);
        } catch (Exception e) {
            throw new PerDecodeException("ReqID decode failed", e);
        }
    }

    /**
     * Encode service-specific fields (after ReqID) into the PER stream.
     */
    protected abstract void encodeBody(PerOutputStream pos);

    /**
     * Decode service-specific fields (after ReqID) from the PER stream.
     */
    protected abstract void decodeBody(PerInputStream pis) throws PerDecodeException;
}
