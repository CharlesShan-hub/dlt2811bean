package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class TestServer extends BaseServerHandler {

    public TestServer() {
        super(ServiceName.TEST);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType req, int reqId) {
        log.info("Test ping from {}", session.getSessionId());
        return buildSuccess(new byte[0], reqId);
    }
}
