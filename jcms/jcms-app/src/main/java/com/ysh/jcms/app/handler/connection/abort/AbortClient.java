package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseHandler;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

/**
 * Client-side handler for Abort service (one-way, no response).
 *
 * <p>
 * Sends an Abort-RequestPDU and immediately closes the session. No response is
 * expected.
 */
public class AbortClient extends BaseClientHandler<AbortDao> {

    @Override
    public void execute(AbortDao dao) throws Exception {
        sendOneWay(ServiceName.ABORT, dao.toRequest());
    }

    @Override
    protected void onSuccess(Frame frame) {
        BaseHandler.traceSession("Aborted");
        node.client().close();
    }
}
