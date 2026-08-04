package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

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
    public void all_data_ld() throws Exception {
        associate();
        long t0 = System.currentTimeMillis();
        final AtomicReference<Exception> err = new AtomicReference<>();
        Thread t = new Thread(() -> {
            try {
                clientNode().getClient(AllDataValuesClient.class)
                        .execute(new AllDataValuesDao().ldName("C1"));
            } catch (Exception e) {
                err.set(e);
            }
        });
        t.start();
        t.join(8000); // 超过客户端 5s 超时仍不返回 = 卡住
        long elapsed = System.currentTimeMillis() - t0;
        if (t.isAlive()) {
            System.out.println("STUCK: no return after " + elapsed + "ms");
            t.interrupt();
        } else if (err.get() != null) {
            System.out.println("ERROR: " + err.get());
        } else {
            System.out.println("OK elapsed=" + elapsed + "ms entries="
                    + clientNode().getContentManager().getAllDataEntries().size());
        }
    }
}
