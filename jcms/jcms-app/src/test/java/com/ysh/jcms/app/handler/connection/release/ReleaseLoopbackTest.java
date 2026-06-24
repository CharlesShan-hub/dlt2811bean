package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.ServerSocket;

import static org.junit.Assert.*;

public class ReleaseLoopbackTest {

    private static int PORT;
    private CmsNode serverNode;
    private CmsNode clientNode;

    @Before
    public void setup() throws Exception {
        PORT = findFreePort();

        serverNode = new CmsNode(PORT);
        serverNode.registerServer(new AssociateServer());
        serverNode.registerServer(new ReleaseServer());
        serverNode.start();

        clientNode = new CmsNode(0);
        clientNode.registerClient(new AssociateClient(clientNode));
        clientNode.registerClient(new ReleaseClient(clientNode));
        clientNode.connect("127.0.0.1", PORT);
    }

    @After
    public void cleanup() {
        if (clientNode != null) clientNode.close();
        if (serverNode != null) serverNode.stop();
    }

    @Test
    public void associate_then_release() throws Exception {
        // First associate
        AssociateClientDao dao = new AssociateClientDao();
        dao.sapRef = "IED1/AP1";
        dao.secure = false;
        clientNode.execute(AssociateClient.class, dao);
        assertEquals(SessionState.ASSOCIATED, clientNode.getClient().getSession().getState());
        assertNotNull(clientNode.getClient().getSession().getAssociationId());

        // Then release
        clientNode.execute(ReleaseClient.class);
        assertEquals(SessionState.CONNECTED, clientNode.getClient().getSession().getState());
        assertNull(clientNode.getClient().getSession().getAssociationId());
    }

    @Test
    public void release_without_associate_should_fail() throws Exception {
        try {
            clientNode.execute(ReleaseClient.class);
            fail("Should throw when releasing without association");
        } catch (Exception e) {
            // Expected
        }
    }

    private static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (Exception e) {
            return 18780;
        }
    }
}
