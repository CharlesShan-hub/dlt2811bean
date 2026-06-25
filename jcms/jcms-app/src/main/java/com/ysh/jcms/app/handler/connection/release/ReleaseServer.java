package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsReleaseRequest;
import com.ysh.jcms.svc.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

/**
 * Server-side handler for incoming Release-RequestPDU.
 *
 * <p>Clears the association and returns a Release-ResponsePDU.
 */
public class ReleaseServer extends BaseServerHandler {

    public ReleaseServer() {
        super(ServiceName.RELEASE);
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsReleaseRequest req = new CmsReleaseRequest();
        if (!tryDecode(session, request, req)) {
            return buildReleaseError(0, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        int reqId = req.reqId.value();
        log.info("Release request from {}: reqId={}", session.getSessionId(), reqId);

        if (!session.isAssociated()) {
            return buildReleaseError(reqId, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }

        byte[] respBytes = new CmsReleaseResponse()
            .reqId(reqId)
            .serviceError(CmsServiceError.NO_ERROR)
            .encode();

        session.clearAssociationId();
        session.setState(SessionState.CONNECTED);
        log.info("Release completed: session={}", session.getSessionId());

        return buildSuccess(respBytes, reqId);
    }

    private Frame buildReleaseError(int reqId, int errorCode) {
        return buildError(new CmsReleaseResponse()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode(), reqId);
    }
}
