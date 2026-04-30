package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.transport.protocol.handlers.AssociateHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.ReleaseHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.TestHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsClient / CmsServer loopback")
class CmsLoopbackTest {

    static final int PORT = 18765;

    CmsServer server;
    CmsClient client;

    private void startServer() throws Exception {
        server = new CmsServer(PORT);
        server.registerHandler(new AssociateHandler());
        server.registerHandler(new ReleaseHandler());
        server.registerHandler(new TestHandler());
        server.start();
        while (!server.isBound()) {
            Thread.sleep(10);
        }
    }

    private void connectClient() throws Exception {
        client = new CmsClient();
        client.connect("127.0.0.1", PORT);
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
    @DisplayName("Associate → positive response + 64-byte associationId")
    void associate() throws Exception {
        startServer();
        connectClient();

        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED1", "AP1")
            .reqId(1);

        CmsApdu response = client.associate(asdu);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        assertEquals(64, client.getAssociationId().length);
    }

    @Test
    @DisplayName("Associate accepts any AP (server does not validate)")
    void associateAny() throws Exception {
        startServer();
        connectClient();

        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("UNKNOWN", "AP1")
            .reqId(2);

        CmsApdu response = client.associate(asdu);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }

    @Test
    @DisplayName("Associate then Release")
    void release() throws Exception {
        startServer();
        connectClient();

        CmsAssociate assoc = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED1", "AP1")
            .reqId(3);
        client.associate(assoc);
        assertNotNull(client.getAssociationId());

        CmsRelease rel = new CmsRelease(MessageType.REQUEST)
            .reqId(4);
        CmsApdu response = client.release(rel);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNull(client.getAssociationId());
    }

    @Test
    @DisplayName("Server disconnect causes client to detect")
    void disconnect() throws Exception {
        // Start server with a delayed stop (closes after 500ms)
        startServer();
        connectClient();

        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
            .serverAccessPointReference("IED1", "AP1")
            .reqId(5);
        client.associate(asdu);

        // Server stops in background, client should detect
        new Thread(() -> {
            try {
                server.stop(500);
            } catch (InterruptedException ignored) {}
        }, "server-stopper").start();

        // Give time for disconnect to propagate
        Thread.sleep(800);
        assertFalse(client.isConnected());
    }
}
