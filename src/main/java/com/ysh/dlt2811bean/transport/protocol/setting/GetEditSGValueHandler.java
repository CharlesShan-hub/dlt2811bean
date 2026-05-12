package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetEditSGValueHandler extends AbstractCmsServiceHandler<CmsGetEditSGValue> {

    public GetEditSGValueHandler() {
        super(ServiceName.GET_EDIT_SG_VALUE, CmsGetEditSGValue::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsGetEditSGValue asdu = (CmsGetEditSGValue) request.getAsdu();

        log.debug("[Server] GetEditSGValue: {} entries", asdu.data.size());

        CmsGetEditSGValue response = new CmsGetEditSGValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.value = new CmsStructure();
        response.moreFollows.set(false);

        return new CmsApdu(response);
    }
}
