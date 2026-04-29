package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLoopback")
class CmsLoopbackTest {

    public static final String serverIP = "127.0.0.1";
    public static final int serverPort = 8080;

    CmsServerTransport server;
    CmsClientTransport client;
    CmsConnection clientConn;
    CountDownLatch serverReceivedLatch;
    CountDownLatch clientReceivedLatch;

    @BeforeEach
    void setUp() throws Exception {
        serverReceivedLatch = new CountDownLatch(1);
        clientReceivedLatch = new CountDownLatch(1);

        server = new CmsServerTransport(serverPort, new CmsTransportListener() {
            @Override
            public void onConnected(CmsConnection connection) {
                System.out.println("[Server] Client connected");
            }

            @Override
            public void onApduReceived(CmsConnection connection, CmsApdu apdu) {
                System.out.println("[Server] Received: " + apdu.getAsdu());
                serverReceivedLatch.countDown();
                if (apdu.getAsdu().getServiceName() == ServiceName.TEST) {
                    try {
                        CmsTest resp = new CmsTest();
                        CmsApdu response = new CmsApdu(resp, MessageType.RESPONSE_POSITIVE);
                        connection.send(response);
                        System.out.println("[Server] Replied: " + resp);
                    } catch (Exception e) {
                        System.err.println("[Server] Reply error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onDisconnected(CmsConnection connection) {
                System.out.println("[Server] Client disconnected");
            }

            @Override
            public void onError(CmsConnection connection, Exception e) {
                System.err.println("[Server] Error: " + e.getMessage());
            }
        });
        server.start();

        client = new CmsClientTransport();
        clientConn = client.connect(serverIP, serverPort, new CmsTransportListener() {
            @Override
            public void onConnected(CmsConnection connection) {
                System.out.println("[Client] Connected");
            }

            @Override
            public void onApduReceived(CmsConnection connection, CmsApdu apdu) {
                System.out.println("[Client] Received: " + apdu.getAsdu());
                clientReceivedLatch.countDown();
            }

            @Override
            public void onDisconnected(CmsConnection connection) {
            }

            @Override
            public void onError(CmsConnection connection, Exception e) {
                System.err.println("[Client] Error: " + e.getMessage());
            }
        });
        clientConn.startReadLoop();
    }

    @AfterEach
    void tearDown() {
        if (clientConn != null) clientConn.close();
        if (server != null) server.stop();
    }

    @Test
    void testLoopback() throws Exception {
        CmsTest test = new CmsTest();
        CmsApdu request = new CmsApdu(test, MessageType.REQUEST);
        System.out.println("[Client] Sending: " + test);
        clientConn.send(request);

        assertTrue(serverReceivedLatch.await(3, TimeUnit.SECONDS), "Server should receive APDU within 3s");
        assertTrue(clientReceivedLatch.await(3, TimeUnit.SECONDS), "Client should receive reply within 3s");
    }
}
