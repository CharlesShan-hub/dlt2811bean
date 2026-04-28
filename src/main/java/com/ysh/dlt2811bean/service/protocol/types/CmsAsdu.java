package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
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
public abstract class CmsAsdu<T extends CmsAsdu<T>> extends AbstractCmsCompound<T> {

    public MessageType messageType;
    public CmsInt16U reqId = new CmsInt16U(0);

    protected CmsAsdu(MessageType messageType) {
        super("ASDU");
        this.messageType = messageType;
        registerField("reqId");
    }

    public abstract ServiceCode getServiceCode();

    @SuppressWarnings("unchecked")
    public T reqId(int value) {
        this.reqId.set(value);
        return (T) this;
    }

    protected static MessageType getReqMessageType(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST;
        if (resp && !err) return MessageType.REQUEST_PLUS;
        throw new IllegalArgumentException("Abort does not support err=true");
    }

    protected static MessageType getRRMessageType(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST;
        if (resp && !err) return MessageType.RESPONSE_POSITIVE;
        if (resp) return MessageType.RESPONSE_NEGATIVE;
        throw new IllegalArgumentException("RR mode does not support !resp && err");
    }

}
