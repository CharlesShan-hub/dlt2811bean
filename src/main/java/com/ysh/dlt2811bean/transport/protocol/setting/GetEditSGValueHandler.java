package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetEditSGValueHandler extends AbstractCmsServiceHandler<CmsGetEditSGValue> {

    public GetEditSGValueHandler() {
        super(ServiceName.GET_EDIT_SG_VALUE, CmsGetEditSGValue::new);
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetEditSGValue: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetEditSGValue) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsGetEditSGValue asdu = (CmsGetEditSGValue) request.getAsdu();

        log.debug("[Server] GetEditSGValue: {} entries", asdu.data.size());

        CmsGetEditSGValue response = new CmsGetEditSGValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.value = new CmsStructure().capacity(100);
        response.moreFollows.set(false);

        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsGetEditSGValue request, int errorCode) {
        CmsGetEditSGValue response = new CmsGetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
