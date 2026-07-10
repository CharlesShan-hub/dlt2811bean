package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class SvrDirLoopbackTest extends BaseLoopbackTest {

    public SvrDirLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new SvrDirServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
        regClient(node, new SvrDirClient(node));
    }

    @Test
    public void get_server_directory() throws Exception {
        associate();

        clientNode().getClient(SvrDirClient.class).execute(new SvrDirDao());

        assertEquals(1, clientNode().getContentManager().getLdNames().size());
        assertTrue(clientNode().getContentManager().getLdNames().contains("C1"));
    }
}
