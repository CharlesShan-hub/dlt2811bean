package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAbortReason;
import com.ysh.jcms.utils.transport.session.SessionState;
import com.ysh.jcms.utils.transport.session.Session;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbortLoopbackTest extends BaseLoopbackTest {

    public AbortLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new AbortServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
        regClient(node, new AbortClient(node));
    }

    @Test
    public void associate_then_abort() throws Exception {
        clientNode().getClient(AssociateClient.class).execute(new AssociateClientDao().sapRef("E1Q1SB1/S1").secure(false));

        Session session = clientNode().getClient().getSession();

        assertEquals(SessionState.ASSOCIATED, session.getState());
        assertNotNull(session.getAssociationId());

        clientNode().getClient(AbortClient.class).execute(new AbortClientDao().reason(CmsAbortReason.INVALID_ARGUMENT));

        assertEquals(SessionState.DISCONNECTED, session.getState());
        assertNull(session.getAssociationId());
    }
}
