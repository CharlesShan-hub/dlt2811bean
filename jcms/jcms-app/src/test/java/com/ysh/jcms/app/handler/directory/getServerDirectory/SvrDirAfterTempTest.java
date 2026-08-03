package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SvrDirAfterTempTest extends BaseLoopbackTest {

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
    public void debug_resp() throws Exception {
        associate();
        CmsGetServerDirectoryRequest req = new CmsGetServerDirectoryRequest().objectClass(1);
        Frame f = clientNode().sendRequest(ServiceName.GET_SERVER_DIRECTORY, req.encode());
        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse();
        resp.decode(f.asduBytes());
        System.out.println("REF_COUNT=" + resp.reference.size());
        for (CmsObjectReference r : resp.reference) {
            System.out.println("REF=[" + r.value() + "]");
        }
        System.out.println("RESP_JSON=" + resp);
    }

    @Test
    public void debug_after() throws Exception {
        associate();
        SvrDirClient c = clientNode().getClient(SvrDirClient.class);

        c.execute(new SvrDirDao());
        List<String> all = new ArrayList<>(clientNode().getContentManager().getLdNames());
        System.out.println("ALL=" + all);

        String last = "C1";
        c.execute(new SvrDirDao().referenceAfter(last));
        System.out.println("AFTER last=" + last + " -> " + clientNode().getContentManager().getLdNames());
    }
}
