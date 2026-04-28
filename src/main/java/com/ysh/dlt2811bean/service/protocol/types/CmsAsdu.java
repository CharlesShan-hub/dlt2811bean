package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ASDU (Application Service Data Unit) — the service-specific payload.
 *
 * <p>ASDU is the payload portion of an APDU, consisting of a ReqID
 * (2-byte unsigned integer) followed by service-specific data fields.
 *
 * <p><b>ASDU Structure:</b></p>
 * <pre>
 * ┌──────────┬────────────────────┐
 * │ ReqID(2B)│ Service Data (var) │
 * └──────────┴────────────────────┘
 * </pre>
 *
 * The ReqID is handled automatically by this base class.
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class CmsAsdu<T extends CmsAsdu<T>> implements CmsType<CmsAsdu<?>> {

    protected MessageType messageType;
    protected CmsInt16U reqId = new CmsInt16U(0);

    protected CmsAsdu(MessageType messageType) {
        this.messageType = messageType;
    }

    public abstract ServiceCode getServiceCode();

    @SuppressWarnings("unchecked")
    public T reqId(int value) {
        this.reqId.set(value);
        return (T) this;
    }

    // ==================== APDU-level encode/decode ====================

    /**
     * Encode this service as a complete ASDU frame (ReqID + ASDU).
     */
    @Override
    public void encode(PerOutputStream pos) {
        reqId.encode(pos);
        switch (messageType) {
            case REQUEST_PLUS:
                encodeRequestPlus(pos);
                break;
            case REQUEST_NEGATIVE:
                encodeRequestNegative(pos);
                break;
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

    /**
     * Decode a complete ASDU frame (ReqID + ASDU) into this service.
     */
    @Override
    @SuppressWarnings("unchecked")
    public CmsAsdu<?> decode(PerInputStream pis) throws Exception {
        reqId.decode(pis);
        switch (messageType) {
            case REQUEST_PLUS:
                decodeRequestPlus(pis);
                break;
            case REQUEST_NEGATIVE:
                decodeRequestNegative(pis); 
                break;
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
        return this;
    }

    // ==================== Subclass Hooks ====================

    protected void encodeRequestPlus(PerOutputStream pos){}
    protected void encodeRequestNegative(PerOutputStream pos){}
    protected void encodeRequest(PerOutputStream pos){}
    protected void encodeResponsePositive(PerOutputStream pos){}
    protected void encodeResponseNegative(PerOutputStream pos){}
    protected void decodeRequestPlus(PerInputStream pis) throws Exception{}
    protected void decodeRequestNegative(PerInputStream pis) throws Exception{}
    protected void decodeRequest(PerInputStream pis) throws Exception{}
    protected void decodeResponsePositive(PerInputStream pis) throws Exception{}
    protected void decodeResponseNegative(PerInputStream pis) throws Exception{}
}
