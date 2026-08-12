package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.connection.CmsAbort;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Abort-RequestPDU (one-way, no response).
 */
public class AbortServer extends BaseServerHandler<CmsAbort, CmsType> {

    public AbortServer() {
        super(CmsServiceInfo.ABORT, CmsAbort.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsAbort req, int reqId) {
        log.warn("Abort received: session={}, reason={}", session.sessionId(), req.reason.value());

        // State hook: entering DISCONNECTED clears session state and closes the
        // connection
        session.state(SessionState.DISCONNECTED);
        return noResponse();
    }
}
