package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.transport.protocol.handlers.AbortHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.AssociateHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abort service loopback tests.
 * Requires Associate as prerequisite.
 * Note: Abort is one-way, no response expected.
 */
@DisplayName("Abort Loopback Test")
class AbortLoopbackTest {

    static final int PORT = 18773;

    CmsServer server;
    CmsClient client;

    private void startServer() throws Exception {
        server = new CmsServer(PORT);
        server.registerHandler(new AssociateHandler());
        server.registerHandler(new AbortHandler());
        server.start();
        while (!server.isBound()) {
            Thread.sleep(10);
        }
    }

    private void connectClient() throws Exception {
        client = new CmsClient();
        client.connect("127.0.0.1", PORT);
    }

    private CmsApdu associate() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference("IED1", "AP1")
                .reqId(1);
        CmsApdu response = client.associate(asdu);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        return response;
    }

    @BeforeEach
    void setup() throws Exception {
        startServer();
        connectClient();
    }

    @AfterEach
    void cleanup() {
        if (client != null) {
            client.close();
            client = null;
        }
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    @DisplayName("Abort → server closes, client sees disconnect")
    void abortNoResponse() throws Exception {
        associate();
        assertTrue(client.isConnected());

        // Abort is one-way: send() returns null without waiting
        CmsAbort request = new CmsAbort(MessageType.REQUEST).reason(0);
        CmsApdu result = client.send(request);
        assertNull(result);  // no response expected for Abort

        // Give time for server to process and close connection
        Thread.sleep(200);
        assertFalse(client.isConnected());
    }

    @Test
    @DisplayName("Re-associate after Abort → succeeds with new session")
    void reassociateAfterAbort() throws Exception {
        // First association
        associate();
        assertNotNull(client.getAssociationId());

        // Abort (one-way, no response)
        CmsAbort abort = new CmsAbort(MessageType.REQUEST).reason(0);
        assertNull(client.send(abort));
        Thread.sleep(200);
        assertFalse(client.isConnected());

        // Reconnect and re-associate
        connectClient();
        CmsApdu response = client.associate(
                new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference("IED1", "AP1")
                        .reqId(1));

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }
}
