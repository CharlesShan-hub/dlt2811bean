package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.utils.transport.ServiceName;

public class TestClient extends BaseClientHandler {

    public void execute() throws Exception {
        send(ServiceName.TEST, new byte[0]);
        log.info("Test ping/pong succeeded");
    }
}
