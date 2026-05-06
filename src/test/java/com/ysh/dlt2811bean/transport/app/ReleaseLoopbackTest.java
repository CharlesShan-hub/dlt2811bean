package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.transport.protocol.association.AssociateHandler;
import com.ysh.dlt2811bean.transport.protocol.association.ReleaseHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Release service loopback tests.
 * Requires Associate as prerequisite.
 */
@DisplayName("Release Loopback Test")
class ReleaseLoopbackTest {

    static final int PORT = 18772;

    CmsServer server;
    CmsClient client;

    private void startServer() throws Exception {
        server = new CmsServer(PORT);
        server.registerHandler(new AssociateHandler());
        server.registerHandler(new ReleaseHandler());
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
    @DisplayName("Associate → Release → positive response, associationId cleared")
    void releasePositive() throws Exception {
        associate();
        assertNotNull(client.getAssociationId());

        CmsRelease request = new CmsRelease(MessageType.REQUEST)
                .reqId(2);
        CmsApdu response = client.release(request);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNull(client.getAssociationId());
    }

    @Test
    @DisplayName("Re-associate after Release → succeeds")
    void reassociateAfterRelease() throws Exception {
        // First association
        associate();
        assertNotNull(client.getAssociationId());

        // Release
        CmsRelease rel = new CmsRelease(MessageType.REQUEST).reqId(2);
        client.release(rel);
        assertNull(client.getAssociationId());

        // Re-associate
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference("IED1", "AP1")
                .reqId(3);
        CmsApdu response = client.associate(asdu);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }
}
