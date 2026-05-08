package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEditSGValueHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetEditSGValueHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_EDIT_SG_VALUE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetEditSGValue: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetEditSGValue) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
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
