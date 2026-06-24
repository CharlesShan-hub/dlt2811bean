package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.svc.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server-side handler for incoming Abort-RequestPDU (one-way, no response).
 *
 * <p>Abort is a one-way message — no response is sent. The session
 * is immediately reset to DISCONNECTED.
 */
public class AbortServer implements ServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(AbortServer.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ABORT;
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsAbort abort = new CmsAbort();
        try {
            abort.decode(request.asduBytes());
        } catch (Exception e) {
            log.error("Failed to decode Abort", e);
            return null;
        }

        int reason = abort.reason.value();
        log.warn("Abort received: session={}, reason={}", session.getSessionId(), reason);

        session.clearAssociationId();
        session.setState(SessionState.DISCONNECTED);

        // One-way — no response
        return null;
    }
}
