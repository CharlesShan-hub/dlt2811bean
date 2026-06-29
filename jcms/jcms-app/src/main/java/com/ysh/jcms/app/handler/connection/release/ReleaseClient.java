package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsReleaseError;
import com.ysh.jcms.svc.connection.CmsReleaseRequest;
import com.ysh.jcms.svc.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;

public class ReleaseClient extends BaseClientHandler {

    public ReleaseClient(CmsNode node) {
        super(node);
    }
    
    public void execute() throws Exception {
        CmsReleaseRequest req = new CmsReleaseRequest()
            .reqId(nextReqId());

        send(ServiceName.RELEASE, req.encode());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsReleaseError err = new CmsReleaseError();
        err.decode(frame.asduBytes());
        throw new IOException("Release rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsReleaseResponse resp = new CmsReleaseResponse();
        resp.decode(frame.asduBytes());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            throw new IOException("Release rejected: error=" + serviceError);
        }

        node.getClient().getSession().clearAssociationId();
        node.getClient().getSession().setState(SessionState.CONNECTED);
        log.info("Release completed");
    }
}
