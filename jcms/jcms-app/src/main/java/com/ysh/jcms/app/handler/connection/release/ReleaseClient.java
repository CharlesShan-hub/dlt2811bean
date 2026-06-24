package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsReleaseRequest;
import com.ysh.jcms.svc.connection.CmsReleaseResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Client-side handler for Release service.
 *
 * <p>Registered via {@link CmsNode#registerClient(Object)}.
 */
public class ReleaseClient {

    private static final Logger log = LoggerFactory.getLogger(ReleaseClient.class);

    private final CmsNode node;

    public ReleaseClient(CmsNode node) {
        this.node = node;
    }

    public void execute() throws Exception {
        byte[] reqBytes = new CmsReleaseRequest()
            .reqId(node.getClient().getSession().nextReqId())
            .encode();

        Frame response = node.sendRequest(ServiceName.RELEASE, reqBytes);
        if (response == null) throw new IOException("Release timeout");

        CmsReleaseResponse resp = new CmsReleaseResponse();
        try { resp.decode(response.asduBytes()); }
        catch (Exception e) { throw new IOException("Failed to decode ReleaseResponse", e); }

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            throw new IOException("Release rejected: error=" + serviceError);
        }

        node.getClient().getSession().clearAssociationId();
        node.getClient().getSession().setState(SessionState.CONNECTED);
        log.info("Release completed");
    }
}
