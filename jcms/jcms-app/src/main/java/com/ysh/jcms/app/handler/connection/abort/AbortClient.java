package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAbort;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Client-side handler for Abort service (one-way, no response).
 *
 * <p>Sends an Abort-RequestPDU and immediately closes the session.
 * No response is expected.
 */
public class AbortClient {

    private static final Logger log = LoggerFactory.getLogger(AbortClient.class);

    private final CmsNode node;

    public AbortClient(CmsNode node) {
        this.node = node;
    }

    public void execute(AbortClientDao dao) throws IOException {
        byte[] reqBytes = new CmsAbort()
            .reqId(node.getClient().getSession().nextReqId())
            .reason(dao.reason)
            .encode();

        // Abort is one-way — fire and forget, no PendingRequest
        int reqId = (reqBytes[0] & 0xFF) << 8 | (reqBytes[1] & 0xFF);
        node.getClient().getConnection().send(new Frame(
            new FrameHeader().serviceCode(ServiceName.ABORT).resp(false).err(false),
            reqBytes, reqId
        ));

        node.getClient().getSession().clearAssociationId();
        node.getClient().getSession().setState(SessionState.DISCONNECTED);
        log.info("Abort sent, reason={}", dao.reason);
    }
}
