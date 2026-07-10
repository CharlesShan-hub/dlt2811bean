package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.svc.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Abort-RequestPDU (one-way, no response).
 */
public class AbortServer extends BaseServerHandler {

    public AbortServer() {
        super(ServiceName.ABORT, CmsAbort.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsAbort req = (CmsAbort) rawReq;
        log.warn("Abort received: session={}, reason={}", session.getSessionId(), req.reason.value());

        session.clear();
        session.setState(SessionState.DISCONNECTED);
        return noResponse();
    }
}
