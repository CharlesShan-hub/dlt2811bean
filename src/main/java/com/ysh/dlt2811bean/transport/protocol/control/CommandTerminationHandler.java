package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCommandTermination;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandTerminationHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandTerminationHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.COMMAND_TERMINATION;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling CommandTermination: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsCommandTermination) request.getAsdu(),
                    CmsAddCause.NOT_SUPPORTED);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsCommandTermination asdu = (CmsCommandTermination) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] CommandTermination: empty reference");
            return buildNegativeResponse(asdu, CmsAddCause.NOT_SUPPORTED);
        }

        CmsCommandTermination response = new CmsCommandTermination(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        log.debug("[Client] CommandTermination acknowledged: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsCommandTermination request, int addCauseCode) {
        CmsCommandTermination response = new CmsCommandTermination(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get());
        return new CmsApdu(response);
    }
}
