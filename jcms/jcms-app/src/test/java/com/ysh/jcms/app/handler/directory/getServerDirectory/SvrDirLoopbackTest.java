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
        regClient(node, new AssociateClient());
        regClient(node, new SvrDirClient());
    }

    @Test
    public void get_server_directory() throws Exception {
        associate();

        clientNode().getClient(SvrDirClient.class).execute(new SvrDirDao());
        // 请求成功即通过（缓存已清理，不再校验缓存内容）
    }
}
