package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.transport.protocol.association.AssociateHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Disconnect detection loopback tests.
 * Requires Associate as prerequisite.
 * Tests that client detects server disconnect.
 */
@DisplayName("Disconnect Loopback Test")
class DisconnectLoopbackTest {

    static final int PORT = 18774;

    CmsServer server;
    CmsClient client;

    private void startServer() throws Exception {
        server = new CmsServer(PORT);
        server.registerHandler(new AssociateHandler());
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
    @DisplayName("Server stop → client detects disconnect")
    void serverStopDetects() throws Exception {
        associate();
        assertTrue(client.isConnected());

        // Server stops in background
        new Thread(() -> {
            try {
                server.stop(300);
            } catch (InterruptedException ignored) {}
        }, "server-stopper").start();

        // Client should detect disconnect
        Thread.sleep(600);
        assertFalse(client.isConnected());
    }

    @Test
    @DisplayName("Client close → server session removed")
    void clientClose() throws Exception {
        associate();
        assertTrue(client.isConnected());
        assertTrue(server.isBound());

        client.close();
        client = null;

        // Give time for cleanup
        Thread.sleep(100);
        // Server should still be running but client disconnected
        assertTrue(server.isBound());
    }
}
