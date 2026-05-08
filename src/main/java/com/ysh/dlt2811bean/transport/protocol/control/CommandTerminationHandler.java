package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCommandTermination;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandTerminationHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandTerminationHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.COMMAND_TERMINATION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling CommandTermination: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsCommandTermination) request.getAsdu(),
                    CmsAddCause.NOT_SUPPORTED);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsCommandTermination asdu = (CmsCommandTermination) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] CommandTermination: empty reference");
            return buildNegativeResponse(asdu, CmsAddCause.NOT_SUPPORTED);
        }

        CmsCommandTermination response = new CmsCommandTermination(MessageType.REQUEST_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] CommandTermination: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsCommandTermination request, int addCauseCode) {
        CmsCommandTermination response = new CmsCommandTermination(MessageType.REQUEST_NEGATIVE)
                .reqId(request.reqId().get())
                .reference(request.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(addCauseCode);
        return new CmsApdu(response);
    }
}
