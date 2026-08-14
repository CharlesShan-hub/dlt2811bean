package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.connection.CmsReleaseError;
import com.ysh.jcms.core.pdu.connection.CmsReleaseRequest;
import com.ysh.jcms.core.pdu.connection.CmsReleaseResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Release-RequestPDU.
 */
public class ReleaseServer extends BaseServerHandler<CmsReleaseRequest, CmsReleaseError> {

    public ReleaseServer() {
        super(CmsServiceInfo.RELEASE, CmsReleaseRequest.class, CmsReleaseError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsReleaseRequest req, int reqId) {
        log.info("Release request from {}: reqId={}", session.sessionId(), reqId);

        if (!session.isAssociated()) {
            return onDecodeError(reqId, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }

        byte[] respBytes = new CmsReleaseResponse().serviceError(CmsServiceError.NO_ERROR).encode();

        // State hook: leaving ASSOCIATED clears association-level state
        session.state(SessionState.CONNECTED);
        log.info("Release completed: session={}", session.sessionId());

        return buildSuccess(respBytes, reqId);
    }
}
