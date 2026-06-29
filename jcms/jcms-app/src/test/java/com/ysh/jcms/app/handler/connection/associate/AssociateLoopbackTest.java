package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.security.SecurityContext;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AssociateLoopbackTest extends BaseLoopbackTest {

    private SecurityContext clientCtx;

    @Override
    @Before
    public void setup() throws Exception {
        clientCtx = SecurityContext.generateSelfSigned();
        super.setup();
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer());
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node));
    }

    // ── without security ──

    @Test
    public void associate_without_security() throws Exception {
        clientNode().getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef("E1Q1SB1/S1").secure(false));

        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());
        assertNotNull(clientNode().getClient().getSession().getAssociationId());
        assertEquals(64, clientNode().getClient().getSession().getAssociationId().length);
    }

    @Test
    public void associate_reject_when_already_associated() throws Exception {
        clientNode().getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef("E1Q1SB1/S1").secure(false));

        try {
            clientNode().getClient(AssociateClient.class)
                .execute(new AssociateClientDao()
                    .sapRef("E1Q1SB1/S1").secure(false));
            fail("Should throw on second associate");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("error="));
        }
    }

    @Test
    public void associate_reject_when_unknown_sap_ref() throws Exception {
        try {
            clientNode().getClient(AssociateClient.class)
                .execute(new AssociateClientDao()
                    .sapRef("NONEXISTENT_AP_X").secure(false));
            fail("Should throw for unknown sapRef");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("error="));
        }
        assertEquals(SessionState.DISCONNECTED, clientNode().getClient().getSession().getState());
    }

    // ── with security ──

    @Test
    public void associate_with_security() throws Exception {
        clientNode().getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef("E1Q1SB1/S1").secure(true));

        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());
        assertNotNull(clientNode().getClient().getSession().getAssociationId());
        assertEquals(64, clientNode().getClient().getSession().getAssociationId().length);
    }

    @Test
    public void associate_secure_reject_when_already_associated() throws Exception {
        clientNode().getClient(AssociateClient.class)
            .execute(new AssociateClientDao()
                .sapRef("E1Q1SB1/S1").secure(true));

        try {
            clientNode().getClient(AssociateClient.class)
                .execute(new AssociateClientDao()
                    .sapRef("E1Q1SB1/S1").secure(true));
            fail("Should throw on second associate");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("error="));
        }
    }
}
