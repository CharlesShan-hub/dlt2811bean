package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCommandTermination;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for CommandTermination service (SC=0x48).
 */
public class CommandTerminationHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.COMMAND_TERMINATION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsCommandTermination asdu = (CmsCommandTermination) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
