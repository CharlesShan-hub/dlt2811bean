package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class TestServer extends BaseServerHandler {

    public TestServer() {
        super(CmsServiceInfo.TEST);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType req, int reqId) {
        log.info("Test ping from {}", session.sessionId());
        return buildSuccess(new byte[0], reqId);
    }
}
