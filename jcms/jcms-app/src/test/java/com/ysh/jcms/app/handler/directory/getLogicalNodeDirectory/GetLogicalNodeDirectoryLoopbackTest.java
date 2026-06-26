package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.directory.CmsAcsiClass;
import org.junit.Test;


public class GetLogicalNodeDirectoryLoopbackTest extends BaseLoopbackTest {

    public GetLogicalNodeDirectoryLoopbackTest() {
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new GetLogicalNodeDirectoryServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
        regClient(node, new GetLogicalNodeDirectoryClient(node));
    }

    @Test
    public void data_set() throws Exception {
        associate();
        clientNode().getClient(GetLogicalNodeDirectoryClient.class)
            .execute(new GetLogicalNodeDirectoryDao()
                .ldName("C1").acsiClass(CmsAcsiClass.DATA_SET));
    }

    @Test
    public void lcb() throws Exception {
        associate();
        clientNode().getClient(GetLogicalNodeDirectoryClient.class)
            .execute(new GetLogicalNodeDirectoryDao()
                .ldName("C1").acsiClass(CmsAcsiClass.LCB));
    }

    @Test
    public void gocb() throws Exception {
        associate();
        clientNode().getClient(GetLogicalNodeDirectoryClient.class)
            .execute(new GetLogicalNodeDirectoryDao()
                .ldName("C1").acsiClass(CmsAcsiClass.GOCB));
    }

    @Test
    public void msvcb() throws Exception {
        associate();
        clientNode().getClient(GetLogicalNodeDirectoryClient.class)
            .execute(new GetLogicalNodeDirectoryDao()
                .ldName("C1").acsiClass(CmsAcsiClass.MSVCB));
    }
}
