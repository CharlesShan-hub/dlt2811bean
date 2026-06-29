package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

/**
 * Client-side handler for Abort service (one-way, no response).
 *
 * <p>Sends an Abort-RequestPDU and immediately closes the session.
 * No response is expected.
 */
public class AbortClient extends BaseClientHandler {

    public AbortClient(CmsNode node) {
        super(node);
    }

    public void execute(AbortClientDao dao) throws Exception {
        CmsAbort req = new CmsAbort()
            .reqId(0)
            .reason(dao.reason());

        sendOneWay(ServiceName.ABORT, req);
    }

    @Override
    protected void onSuccess(Frame frame) {
        node.getClient().close();
        log.info("Abort sent, connection closed.");
    }
}
