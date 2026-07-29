package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.connection.CmsReleaseError;
import com.ysh.jcms.pdu.connection.CmsReleaseRequest;
import com.ysh.jcms.pdu.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Release-RequestPDU.
 */
public class ReleaseServer extends BaseServerHandler {

    public ReleaseServer() {
        super(ServiceName.RELEASE, CmsReleaseRequest.class, CmsReleaseError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsReleaseRequest req = (CmsReleaseRequest) rawReq;
        log.info("Release request from {}: reqId={}", session.getSessionId(), reqId);

        if (!session.isAssociated()) {
            return onDecodeError(reqId, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }

        byte[] respBytes = new CmsReleaseResponse().reqId(reqId).serviceError(CmsServiceError.NO_ERROR).encode();

        session.clear();
        session.setState(SessionState.CONNECTED);
        log.info("Release completed: session={}", session.getSessionId());

        return buildSuccess(respBytes, reqId);
    }
}
