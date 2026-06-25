package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReleaseLoopbackTest extends BaseLoopbackTest {

    public ReleaseLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) {
        regServer(node, new AssociateServer());
        regServer(node, new ReleaseServer());
    }

    @Override
    protected void registerClients(CmsNode node) {
        regClient(node, new AssociateClient(node));
        regClient(node, new ReleaseClient(node));
    }

    @Test
    public void associate_then_release() throws Exception {
        // First associate
        AssociateClientDao dao = new AssociateClientDao()
            .sapRef("IED1/AP1").secure(false);
        exec(clientNode(), AssociateClient.class, dao);
        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());
        assertNotNull(clientNode().getClient().getSession().getAssociationId());

        // Then release
        exec(clientNode(), ReleaseClient.class);
        assertEquals(SessionState.CONNECTED, clientNode().getClient().getSession().getState());
        assertNull(clientNode().getClient().getSession().getAssociationId());
    }

    @Test
    public void release_without_associate_should_fail() throws Exception {
        try {
            exec(clientNode(), ReleaseClient.class);
            fail("Should throw when releasing without association");
        } catch (Exception e) {
            // Expected
        }
    }
}
