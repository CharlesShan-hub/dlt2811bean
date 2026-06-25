package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsReleaseRequest;
import com.ysh.jcms.svc.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;

/**
 * Client-side handler for Release service.
 *
 * <p>Registered via {@link CmsNode#registerClient(Object)}.
 */
public class ReleaseClient extends BaseClientHandler {

    public ReleaseClient(CmsNode node) {
        super(node);
    }

    public void execute() throws Exception {
        byte[] reqBytes = new CmsReleaseRequest()
            .reqId(nextReqId())
            .encode();

        CmsReleaseResponse resp = decodeFrame(
            send(ServiceName.RELEASE, reqBytes),
            new CmsReleaseResponse());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            throw new IOException("Release rejected: error=" + serviceError);
        }

        node.getClient().getSession().clearAssociationId();
        node.getClient().getSession().setState(SessionState.CONNECTED);
        log.info("Release completed");
    }
}
