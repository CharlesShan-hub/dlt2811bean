package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.transport.session.SessionState;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

/**
 * Handler for Abort service (SC=3).
 *
 * <p>Abort is a one-way service with no response.
 * Clears the association ID and lets the connection close naturally.
 */
public class AbortHandler extends AbstractCmsServiceHandler<CmsAbort> {

    public AbortHandler() {
        super(ServiceName.ABORT, CmsAbort::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        serverSession.clearAssociationId();
        serverSession.setState(SessionState.CLOSED);

        log.debug("Association aborted, reason={}", asdu.reason().get());
        serverSession.close();
        return null;
    }
}