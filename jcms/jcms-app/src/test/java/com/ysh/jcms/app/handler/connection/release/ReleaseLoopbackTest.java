package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateDao;
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
        regClient(node, new AssociateClient());
        regClient(node, new ReleaseClient());
    }

    @Test
    public void associate_then_release() throws Exception {
        // First associate
        AssociateClient associate = clientNode().getClient(AssociateClient.class);
        associate.execute(new AssociateDao().sapRef("E1Q1SB1/S1").secure(false));
        assertEquals(SessionState.ASSOCIATED, clientNode().client().session().state());
        assertNotNull(clientNode().client().session().associationId());

        // Then release
        clientNode().getClient(ReleaseClient.class).execute(new ReleaseDao());
        assertEquals(SessionState.CONNECTED, clientNode().client().session().state());
        assertNull(clientNode().client().session().associationId());
    }

    @Test
    public void release_without_associate_should_fail() throws Exception {
        try {
            clientNode().getClient(ReleaseClient.class).execute(new ReleaseDao());
            fail("Should throw when releasing without association");
        } catch (Exception e) {
            // Expected
        }
    }
}
