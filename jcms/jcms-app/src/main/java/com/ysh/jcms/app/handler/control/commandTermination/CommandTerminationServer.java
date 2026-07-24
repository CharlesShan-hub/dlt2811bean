package com.ysh.jcms.app.handler.control.commandTermination;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.svc.control.CmsCommandTermination;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandTerminationServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandTerminationServer.class);

    public CommandTerminationServer() {
        super(ServiceName.COMMAND_TERMINATION, CmsCommandTermination.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsCommandTermination req = (CmsCommandTermination) rawReq;
        String ref = str(req.reference);
        log.info("CommandTermination from {}: reqId={}, ref={}", session.getSessionId(), reqId, ref);
        // Unconfirmed service — no response
        return noResponse();
    }
}
