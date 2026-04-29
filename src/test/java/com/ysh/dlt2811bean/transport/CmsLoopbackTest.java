package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CmsLoopback")
class CmsLoopbackTest {

    public static final String serverIP = "127.0.0.1";
    public static final int serverPort = 8080;

    CmsServer server;
    CmsClient client;

    @BeforeEach
    void setUp() throws Exception {
        server = new CmsServer(serverPort);
        server.run(false);

        client = new CmsClient(serverIP, serverPort);
        client.run(false);
    }

    @AfterEach
    void tearDown() throws Exception {
        client.close(3000);
        server.stop(3000);
    }

    @Test
    void testLoopback() throws Exception {
        CmsTest test = new CmsTest();
        CmsApdu request = new CmsApdu(test, MessageType.REQUEST);
        System.out.println("[Client] Sending: " + test);
        client.send(request);
    }
}
