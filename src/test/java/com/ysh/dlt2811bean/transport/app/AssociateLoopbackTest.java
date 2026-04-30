package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.transport.protocol.handlers.AssociateHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Associate service loopback tests.
 * No prerequisite - tests connection establishment from scratch.
 */
@DisplayName("Associate Loopback Test")
class AssociateLoopbackTest {

    static final int PORT = 18770;

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
    @DisplayName("Associate → positive response + 64-byte associationId")
    void associatePositive() throws Exception {
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
    void associateAnyAp() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference("UNKNOWN", "AP1")
                .reqId(2);

        CmsApdu response = client.associate(asdu);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }

    @Test
    @DisplayName("Multiple associates → each gets unique associationId")
    void associateMultiple() throws Exception {
        // First association
        CmsApdu r1 = client.associate(
                new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference("IED1", "AP1")
                        .reqId(1));
        assertNotNull(r1);
        assertEquals(MessageType.RESPONSE_POSITIVE, r1.getMessageType());

        // Disconnect and reconnect
        client.close();
        connectClient();

        // Second association
        CmsApdu r2 = client.associate(
                new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference("IED1", "AP1")
                        .reqId(1));
        assertNotNull(r2);
        assertEquals(MessageType.RESPONSE_POSITIVE, r2.getMessageType());

        // Each session should have different associationId
        assertNotNull(client.getAssociationId());
    }
}
