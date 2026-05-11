package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public abstract class CmsAsdu<T extends CmsAsdu<T>> extends AbstractCmsCompound<T> {

    @CmsField
    public CmsInt16U reqId = new CmsInt16U(0);

    public MessageType messageType = MessageType.UNKNOWN;

    protected CmsAsdu(ServiceName serviceName) {
        super(serviceName.name());
    }

    protected CmsAsdu(ServiceName serviceName, MessageType messageType) {
        this(serviceName);
        this.messageType = messageType;
    }

    @Override
    protected boolean acceptField(CmsField ann) {
        if (ann.only().length == 0) return true;
        for (MessageType mt : ann.only()) {
            if (mt == messageType) return true;
        }
        return false;
    }

    public ServiceName getServiceName(){
        return ServiceName.fromName(typeName);
    }

    @SuppressWarnings("unchecked")
    public T reqId(int value) {
        this.reqId.set(value);
        return (T) this;
    }

    protected static MessageType getIndicationMessageType(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST;
        if (resp && !err) return MessageType.INDICATION;
        throw new IllegalArgumentException("does not support err=true");
    }

    protected static MessageType getReqMessageType(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST_POSITIVE;
        if (!resp && err) return MessageType.REQUEST_NEGATIVE;
        if (resp && !err) return MessageType.RESPONSE_POSITIVE;
        return MessageType.RESPONSE_NEGATIVE;
    }

    protected static MessageType getRRMessageType(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST;
        if (resp && !err) return MessageType.RESPONSE_POSITIVE;
        if (resp) return MessageType.RESPONSE_NEGATIVE;
        throw new IllegalArgumentException("RR mode does not support !resp && err");
    }
}
