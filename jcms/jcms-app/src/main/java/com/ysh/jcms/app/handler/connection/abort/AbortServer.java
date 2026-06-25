package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.svc.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Abort-RequestPDU (one-way, no response).
 *
 * <p>Abort is a one-way message — no response is sent. The session
 * is immediately reset to DISCONNECTED.
 */
public class AbortServer extends BaseServerHandler {

    public AbortServer() {
        super(ServiceName.ABORT);
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsAbort abort = new CmsAbort();
        if (!tryDecode(session, request, abort)) return noResponse();

        log.warn("Abort received: session={}, reason={}",
            session.getSessionId(), abort.reason.value());

        session.clearAssociationId();
        session.setState(SessionState.DISCONNECTED);

        // One-way — no response
        return noResponse();
    }
}
