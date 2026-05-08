package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for Abort service (SC=3).
 *
 * <p>Abort is a one-way service with no response.
 * Clears the association ID and lets the connection close naturally.
 */
public class AbortHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(AbortHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ABORT;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsAbort asdu = (CmsAbort) request.getAsdu();

        serverSession.clearAssociationId();
        serverSession.setState(SessionState.CLOSED);

        log.debug("Association aborted, reason={}", asdu.reason().get());
        serverSession.close();
        return null;
    }
}