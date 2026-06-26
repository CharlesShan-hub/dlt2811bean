package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetServerDirectoryLoopbackTest extends BaseLoopbackTest {

    public GetServerDirectoryLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new GetServerDirectoryServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
        regClient(node, new GetServerDirectoryClient(node));
    }

    @Test
    public void get_server_directory() throws Exception {
        clientNode().getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef("E1Q1SB1/S1").secure(false));

        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());

        clientNode().getClient(GetServerDirectoryClient.class)
            .execute(new GetServerDirectoryDao());

        assertEquals(1, clientNode().getContentManager().getLdNames().size());
        assertTrue(clientNode().getContentManager().getLdNames().contains("C1"));
    }
}