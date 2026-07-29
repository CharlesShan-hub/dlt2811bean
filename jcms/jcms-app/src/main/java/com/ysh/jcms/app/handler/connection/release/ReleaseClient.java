package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.connection.CmsReleaseError;
import com.ysh.jcms.pdu.connection.CmsReleaseRequest;
import com.ysh.jcms.pdu.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;

public class ReleaseClient extends BaseClientHandler {

    public void execute() throws Exception {
        CmsReleaseRequest req = new CmsReleaseRequest().reqId(nextReqId());

        byte[] assocId = node.getClient().getSession().getAssociationId();
        if (assocId != null && assocId.length > 0) {
            req.assocId(assocId);
        }

        send(ServiceName.RELEASE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsReleaseError err = decodeErr(frame, new CmsReleaseError());
        throw new IOException("Release rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsReleaseResponse resp = decodeResp(frame, new CmsReleaseResponse());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            throw new IOException("Release rejected: " + new CmsServiceError(serviceError).constantName() + " (" + serviceError + ")");
        }

        node.getClient().getSession().clear();
        node.getClient().getSession().setState(SessionState.CONNECTED);
        log.info("Release completed");
    }
}
