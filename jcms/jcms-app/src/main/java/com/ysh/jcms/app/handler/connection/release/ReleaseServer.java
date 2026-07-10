package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsReleaseError;
import com.ysh.jcms.svc.connection.CmsReleaseRequest;
import com.ysh.jcms.svc.connection.CmsReleaseResponse;
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
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsReleaseRequest req = (CmsReleaseRequest) rawReq;
        int reqId = req.reqId.value();
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
