package com.ysh.dlt2811bean.transport.app.associate;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Associate service loopback tests.
 * Tests both plain TCP and TCP with GM authentication.
 */
@DisplayName("Associate Loopback Test")
class AssociateLoopbackTest extends LoopbackTest {

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

        CmsApdu response = client.associate("IED1", "AP1");

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

        CmsApdu response = client.setAccessPoint("CLIENT", "EP1").associate();

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

        CmsApdu response = client.setAccessPoint("CLIENT", "EP2").associate();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        assertEquals(64, client.getAssociationId().length);
        assertTrue(server.isSecurityEnabled());
        assertTrue(client.isSecurityEnabled());

        closeClient();
        closeServer();
    }
}
