package com.ysh.jcms.app.handler.control.timeActivatedOperateTermination;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.control.CmsTimeActivatedOperateTermination;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class TimeActivatedOperateTerminationServer extends BaseServerHandler<CmsTimeActivatedOperateTermination, CmsType> {

    public TimeActivatedOperateTerminationServer() {
        super(CmsServiceInfo.TIME_ACTIVATED_OPERATE_TERMINATION, CmsTimeActivatedOperateTermination.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTimeActivatedOperateTermination req, int reqId) {
        String ref = str(req.reference);
        log.info("TimeActivatedOperateTermination from {}: reqId={}, ref={}", session.sessionId(), reqId, ref);
        return noResponse();
    }
}
