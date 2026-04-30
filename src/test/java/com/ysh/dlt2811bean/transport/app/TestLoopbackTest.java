package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.protocol.handlers.AssociateHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.TestHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test service (keep-alive) loopback tests.
 * Requires Associate as prerequisite.
 */
@DisplayName("Test Loopback Test")
class TestLoopbackTest {

    static final int PORT = 18771;

    CmsServer server;
    CmsClient client;

    private void startServer() throws Exception {
        server = new CmsServer(PORT);
        server.registerHandler(new AssociateHandler());
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
    @DisplayName("Test request → echo received")
    void testPositive() throws Exception {
        associate();

        CmsTest request = new CmsTest(MessageType.REQUEST);
        CmsApdu response = client.test(request);

        assertNotNull(response, "Test echo should arrive within timeout");
    }

    @Test
    @DisplayName("Multiple Test requests → all succeed")
    void testMultiple() throws Exception {
        associate();

        for (int i = 0; i < 5; i++) {
            CmsTest request = new CmsTest(MessageType.REQUEST);
            CmsApdu response = client.test(request);
            assertNotNull(response, "Test #" + (i + 1) + " echo should arrive");
        }
    }
}
