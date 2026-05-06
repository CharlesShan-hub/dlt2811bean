package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.SessionState;

/**
 * Handler for Abort service (SC=3).
 *
 * <p>Abort is a one-way service with no response.
 * Clears the association ID and lets the connection close naturally.
 */
public class AbortHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ABORT;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsAbort asdu = (CmsAbort) request.getAsdu();

        // Clear association
        session.clearAssociationId();
        session.setState(SessionState.CLOSED);

        System.out.println("[Server] Association aborted, reason=" + asdu.reason().get());
        // No response — Abort is one-way, close the connection
        session.close();
        return null;
    }
}