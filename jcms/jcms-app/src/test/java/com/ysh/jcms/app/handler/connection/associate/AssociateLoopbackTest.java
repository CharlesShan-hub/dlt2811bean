package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class AssociateLoopbackTest {

    private static int PORT;
    private CmsNode serverNode;
    private CmsNode clientNode;

    @Before
    public void setup() throws Exception {
        PORT = findFreePort();

        serverNode = new CmsNode(PORT);
        serverNode.registerServer(new AssociateServer());
        serverNode.start();

        clientNode = new CmsNode(0);
        clientNode.registerClient(new AssociateClient(clientNode));
        clientNode.connect("127.0.0.1", PORT);
    }

    @After
    public void cleanup() {
        if (clientNode != null) clientNode.close();
        if (serverNode != null) serverNode.stop();
    }

    @Test
    public void associate_without_security() throws Exception {
        AssociateClientDao dao = new AssociateClientDao();
        dao.sapRef = "IED1/AP1";
        dao.secure = false;

        CmsAssociateResponse resp = clientNode.execute(AssociateClient.class, dao);

        assertNotNull(resp);
        assertEquals(0, resp.serviceError.value());
        assertEquals(64, resp.assocId.len);
        assertEquals(SessionState.ASSOCIATED, clientNode.getClient().getSession().getState());
        assertNotNull(clientNode.getClient().getSession().getAssociationId());
    }

    @Test
    public void associate_reject_when_already_associated() throws Exception {
        AssociateClientDao dao = new AssociateClientDao();
        dao.sapRef = "IED1/AP1";
        dao.secure = false;

        clientNode.execute(AssociateClient.class, dao);

        try {
            clientNode.execute(AssociateClient.class, dao);
            fail("Should throw on second associate");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("error=2"));
        }
    }

    private static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            return 18780;
        }
    }
}
