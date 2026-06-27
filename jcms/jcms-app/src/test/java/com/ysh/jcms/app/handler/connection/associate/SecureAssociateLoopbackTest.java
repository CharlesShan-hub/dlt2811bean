package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.security.SecurityContext;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecureAssociateLoopbackTest extends BaseLoopbackTest {

    private SecurityContext ctx;

    @Override
    @Before
    public void setup() throws Exception {
        ctx = SecurityContext.generateSelfSigned();
        super.setup();
    }

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        regServer(node, new AssociateServer().enableSecurity(ctx));
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        regClient(node, new AssociateClient(node, ctx));
    }

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
        } catch (java.io.IOException e) {
            assertTrue(e.getMessage().contains("error="));
        }
    }
}
