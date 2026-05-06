package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * service loopback tests basic
 */
public class LoopbackTest {

    protected static final Logger log = LoggerFactory.getLogger(LoopbackTest.class);

    static final int PORT = 18773;

    public CmsServer server;
    public CmsClient client;

    public void startServer() throws Exception {
        server = new CmsServer(PORT);
        server.registerDefaultHandlers();
        server.start();
        while (!server.isBound()) {
            Thread.sleep(10);
        }
    }

    public void connectClient() throws Exception {
        client = new CmsClient();
        client.setAccessPoint("IED1", "AP1");
        client.connect("127.0.0.1", PORT);
    }

    public CmsApdu associate() throws Exception {
        CmsApdu response = client.associate();
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
}
