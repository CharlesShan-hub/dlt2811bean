package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.session.SessionState;
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
        byte[] reqBytes = new CmsAbort()
            .reqId(0)       // ReqID=0 for one-way (non-request-response) services per DL/T 2811 §6.2.1-c
            .reason(dao.reason())
            .encode();

        sendOneWay(ServiceName.ABORT, reqBytes);
    }

    @Override
    protected void onSuccess(Frame frame) {
        node.getClient().getSession().clearAssociationId();
        node.getClient().getSession().setState(SessionState.DISCONNECTED);
        log.info("Abort sent, reason=...");
    }
}
