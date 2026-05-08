package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelect;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SelectHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling Select: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsSelect) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSelect asdu = (CmsSelect) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] Select: empty reference");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsSelect response = new CmsSelect(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref);

        log.debug("[Server] Select: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsSelect request, int errorCode) {
        CmsSelect response = new CmsSelect(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .reference(request.reference.get());
        return new CmsApdu(response);
    }
}
