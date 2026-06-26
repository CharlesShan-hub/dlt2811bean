package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class TestServer extends BaseServerHandler {

    public TestServer() {
        super(ServiceName.TEST);
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        log.info("Test ping from {}", session.getSessionId());
        return buildSuccess(new byte[0], 0);
    }
}
