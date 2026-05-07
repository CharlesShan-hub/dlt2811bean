package com.ysh.dlt2811bean.transport.app.associate;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Associate service loopback tests.
 * Tests both plain TCP and TCP with GM authentication.
 *
 * <p>Uses manual lifecycle because each test method requires
 * different server/client security configurations.
 */
@DisplayName("Associate Loopback Test")
class AssociateLoopbackTest extends LoopbackTest {

    @Override
    protected boolean useAutoLifecycle() {
        return false;
    }

    @Test
    @DisplayName("Associate → default access point, no authentication, no security")
    void associatePositive() throws Exception {

        startServer(false);
        startClient(false);

        CmsApdu response = associate();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertEquals(64, client.getAssociationId().length);

        closeClient();
        closeServer();
    }

    @Test
    @DisplayName("Associate → custom access point, no authentication, no security")
    void associateWithAccessPoint() throws Exception {

        startServer(false);
        startClient(false);

        CmsApdu response = client.associate("E1Q1SB1", "S1");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());

        closeClient();
        closeServer();
    }

    @Test
    @DisplayName("Associate → client with security, server without")
    void associateClientSecurityOnly() throws Exception {
        
        startServer(false);
        startClient(true);

        CmsApdu response = client.setAccessPoint("E1Q1SB1", "S1").associate();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        assertEquals(64, client.getAssociationId().length);
        assertFalse(server.isSecurityEnabled());
        assertTrue(client.isSecurityEnabled());

        closeClient();
        closeServer();
    }

    @Test
    @DisplayName("Associate → both server and client with security")
    void associateWithSecurity() throws Exception {
        
        startServer(true);
        startClient(true);

        CmsApdu response = client.setAccessPoint("E1Q1SB1", "S1").associate();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        assertEquals(64, client.getAssociationId().length);
        assertTrue(server.isSecurityEnabled());
        assertTrue(client.isSecurityEnabled());

        closeClient();
        closeServer();
    }
}
