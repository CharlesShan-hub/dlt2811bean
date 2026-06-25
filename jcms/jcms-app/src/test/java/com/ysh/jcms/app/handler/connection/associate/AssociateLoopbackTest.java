package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AssociateLoopbackTest extends BaseLoopbackTest {

    public AssociateLoopbackTest() {
        // must use registerServers/registerClients; see superclass
    }

    @Override
    protected void registerServers(CmsNode node) {
        regServer(node, new AssociateServer());
    }

    @Override
    protected void registerClients(CmsNode node) {
        regClient(node, new AssociateClient(node));
    }

    @Test
    public void associate_without_security() throws Exception {
        AssociateClientDao dao = new AssociateClientDao()
            .sapRef("IED1/AP1").secure(false);

        CmsAssociateResponse resp = exec(clientNode(), AssociateClient.class, dao);

        assertNotNull(resp);
        assertEquals(0, resp.serviceError.value());
        assertEquals(64, resp.assocId.len);
        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());
        assertNotNull(clientNode().getClient().getSession().getAssociationId());
    }

    @Test
    public void associate_reject_when_already_associated() throws Exception {
        AssociateClientDao dao = new AssociateClientDao()
            .sapRef("IED1/AP1").secure(false);

        exec(clientNode(), AssociateClient.class, dao);

        try {
            exec(clientNode(), AssociateClient.class, dao);
            fail("Should throw on second associate");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("error=2"));
        }
    }
}
