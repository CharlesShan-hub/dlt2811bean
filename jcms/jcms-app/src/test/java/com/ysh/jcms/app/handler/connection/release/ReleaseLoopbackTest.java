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
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new ReleaseServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
        regClient(node, new ReleaseClient(node));
    }

    @Test
    public void associate_then_release() throws Exception {
        // First associate
        AssociateClient associate = clientNode().getClient(AssociateClient.class);
        associate.execute(new AssociateClientDao()
            .sapRef("E1Q1SB1/S1").secure(false));
        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());
        assertNotNull(clientNode().getClient().getSession().getAssociationId());

        // Then release
        clientNode().getClient(ReleaseClient.class).execute();
        assertEquals(SessionState.CONNECTED, clientNode().getClient().getSession().getState());
        assertNull(clientNode().getClient().getSession().getAssociationId());
    }

    @Test
    public void release_without_associate_should_fail() throws Exception {
        try {
            clientNode().getClient(ReleaseClient.class).execute();
            fail("Should throw when releasing without association");
        } catch (Exception e) {
            // Expected
        }
    }
}
