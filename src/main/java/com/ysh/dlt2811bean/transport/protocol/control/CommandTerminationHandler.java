package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCommandTermination;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class CommandTerminationHandler extends AbstractCmsServiceHandler<CmsCommandTermination> {

    public CommandTerminationHandler() {
        super(ServiceName.COMMAND_TERMINATION, CmsCommandTermination::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsCommandTermination asdu = (CmsCommandTermination) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] CommandTermination: empty reference");
            return buildNegativeResponse(request, CmsAddCause.NOT_SUPPORTED);
        }

        CmsCommandTermination response = new CmsCommandTermination(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        log.debug("[Client] CommandTermination acknowledged: {}", ref);
        return new CmsApdu(response);
    }
}
