package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.data.enumerate.CmsAbortReason;
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
        regClient(node, new AssociateClient());
        regClient(node, new AbortClient());
    }

    @Test
    public void associate_then_abort() throws Exception {
        clientNode().getClient(AssociateClient.class).execute(new AssociateDao().sapRef("E1Q1SB1/S1").secure(false));

        Session session = clientNode().client().session();

        assertEquals(SessionState.ASSOCIATED, session.state());
        assertNotNull(session.associationId());

        clientNode().getClient(AbortClient.class).execute(new AbortDao().reason(CmsAbortReason.INVALID_ARGUMENT));

        assertEquals(SessionState.DISCONNECTED, session.state());
        assertNull(session.associationId());
    }
}
