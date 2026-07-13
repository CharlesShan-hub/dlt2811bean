package com.ysh.jcms.app.handler.control.timeActivatedOperateTermination;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.svc.control.CmsTimeActivatedOperateTermination;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeActivatedOperateTerminationServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(TimeActivatedOperateTerminationServer.class);

    public TimeActivatedOperateTerminationServer() {
        super(ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION, CmsTimeActivatedOperateTermination.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsTimeActivatedOperateTermination req = (CmsTimeActivatedOperateTermination) rawReq;
        String ref = str(req.reference);
        log.info("TimeActivatedOperateTermination from {}: reqId={}, ref={}", session.getSessionId(), reqId, ref);
        return noResponse();
    }
}
