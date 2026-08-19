package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import org.junit.Test;

public class AllDataValuesTempTest extends BaseLoopbackTest {

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
        regServer(node, new AllDataValuesServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient());
        regClient(node, new AllDataValuesClient());
    }

    @Test
    public void stress_50() throws Exception {
        associate();
        int ok = 0;
        int err = 0;
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            try {
                clientNode().getClient(AllDataValuesClient.class).execute(new AllDataValuesDao().reference("C1"));
                ok++;
            } catch (Exception e) {
                err++;
                System.out.println("iter " + i + " FAIL: " + e);
            }
        }
        long elapsed = System.currentTimeMillis() - t0;
        System.out.println("stress done: ok=" + ok + " err=" + err + " elapsed=" + elapsed + "ms");
    }
}
