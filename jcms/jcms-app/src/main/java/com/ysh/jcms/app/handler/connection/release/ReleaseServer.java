package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsReleaseRequest;
import com.ysh.jcms.svc.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server-side handler for incoming Release-RequestPDU.
 *
 * <p>Clears the association and returns a Release-ResponsePDU.
 */
public class ReleaseServer implements ServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(ReleaseServer.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.RELEASE;
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsReleaseRequest req = new CmsReleaseRequest();
        try {
            req.decode(request.asduBytes());
        } catch (Exception e) {
            log.error("Failed to decode ReleaseRequest", e);
            return buildErrorResponse(request.reqId(), CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        int reqId = req.reqId.value();
        log.info("Release request from {}: reqId={}", session.getSessionId(), reqId);

        if (!session.isAssociated()) {
            return buildErrorResponse(reqId, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }

        byte[] respBytes = new CmsReleaseResponse()
            .reqId(reqId)
            .serviceError(CmsServiceError.NO_ERROR)
            .encode();

        session.clearAssociationId();
        session.setState(SessionState.CONNECTED);

        log.info("Release completed: session={}", session.getSessionId());

        return new Frame(
            new FrameHeader().serviceCode(ServiceName.RELEASE).resp(true).err(false),
            respBytes, reqId
        );
    }

    private Frame buildErrorResponse(int reqId, int errorCode) {
        byte[] respBytes = new CmsReleaseResponse()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode();

        return new Frame(
            new FrameHeader().serviceCode(ServiceName.RELEASE).resp(true).err(true),
            respBytes, reqId
        );
    }
}
