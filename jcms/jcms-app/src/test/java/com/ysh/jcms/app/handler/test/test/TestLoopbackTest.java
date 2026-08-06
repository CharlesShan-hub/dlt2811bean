package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import org.junit.Test;

public class TestLoopbackTest extends BaseLoopbackTest {

    public TestLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new TestServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new TestClient());
    }

    @Test
    public void ping_pong() throws Exception {
        clientNode().getClient(TestClient.class).execute(new TestDao());
    }
}
