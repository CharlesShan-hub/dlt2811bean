package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.transport.session.Session;
import org.junit.Test;

import static org.junit.Assert.*;

public class NegotiateLoopbackTest extends BaseLoopbackTest {

    public NegotiateLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new NegotiateServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new NegotiateClient());
    }

    @Test
    public void negotiate() throws Exception {
        clientNode().getClient(NegotiateClient.class).execute(new NegotiateDao());

        Session session = clientNode().client().session();
        assertTrue(session.negotiated());
        assertTrue(session.negotiatedApduSize() > 0);
    }
}
