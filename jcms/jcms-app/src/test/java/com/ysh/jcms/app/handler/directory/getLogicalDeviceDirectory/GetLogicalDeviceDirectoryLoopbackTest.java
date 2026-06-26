package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
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
        associate();

        clientNode().getClient(GetLogicalDeviceDirectoryClient.class)
            .execute(new GetLogicalDeviceDirectoryDao()
                .ldName("C1"));

        assertEquals(6, clientNode().getContentManager().getLnNames().size());
        assertTrue(clientNode().getContentManager().getLnNames().contains("LLN0"));
        assertTrue(clientNode().getContentManager().getLnNames().contains("CSWI1"));
        assertTrue(clientNode().getContentManager().getLnNames().contains("MMXU1"));
    }
}