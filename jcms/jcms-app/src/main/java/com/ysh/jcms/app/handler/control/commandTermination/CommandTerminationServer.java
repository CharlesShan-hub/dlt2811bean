package com.ysh.jcms.app.handler.control.commandTermination;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.control.CmsCommandTermination;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class CommandTerminationServer extends BaseServerHandler<CmsCommandTermination, CmsType> {

    public CommandTerminationServer() {
        super(CmsServiceInfo.COMMAND_TERMINATION, CmsCommandTermination.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsCommandTermination req, int reqId) {
        String ref = str(req.reference);
        log.info("CommandTermination from {}: reqId={}, ref={}", session.sessionId(), reqId, ref);
        // Unconfirmed service — no response
        return noResponse();
    }
}
