package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
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
        associate();

        clientNode().getClient(GetServerDirectoryClient.class)
            .execute(new GetServerDirectoryDao());

        assertEquals(1, clientNode().getContentManager().getLdNames().size());
        assertTrue(clientNode().getContentManager().getLdNames().contains("C1"));
    }
}