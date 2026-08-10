package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Abort-RequestPDU (one-way, no response).
 */
public class AbortServer extends BaseServerHandler<CmsAbort, CmsType> {

    public AbortServer() {
        super(ServiceName.ABORT, CmsAbort.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsAbort req, int reqId) {
        log.warn("Abort received: session={}, reason={}", session.sessionId(), req.reason.value());

        session.clear();
        session.state(SessionState.DISCONNECTED);
        return noResponse();
    }
}
