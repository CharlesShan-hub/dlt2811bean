package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetLogicalDeviceDirectoryLoopbackTest extends BaseLoopbackTest {

    public GetLogicalDeviceDirectoryLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new GetLogicalDeviceDirectoryServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
        regClient(node, new GetLogicalDeviceDirectoryClient(node));
    }

    @Test
    public void get_logical_device_directory() throws Exception {
        clientNode().getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef("E1Q1SB1/S1").secure(false));

        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());

        clientNode().getClient(GetLogicalDeviceDirectoryClient.class)
            .execute(new GetLogicalDeviceDirectoryDao()
                .ldName("C1"));

        assertEquals(6, clientNode().getContentManager().getLnNames().size());
        assertTrue(clientNode().getContentManager().getLnNames().contains("LLN0"));
        assertTrue(clientNode().getContentManager().getLnNames().contains("CSWI1"));
        assertTrue(clientNode().getContentManager().getLnNames().contains("MMXU1"));
    }
}