package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * service loopback tests basic
 */
public class LoopbackTest {

    protected static final Logger log = LoggerFactory.getLogger(LoopbackTest.class);

    protected static final int PORT = 18773;
    private static final int MAX_WAIT_SECONDS = 30;
    private static final int RETRY_INTERVAL_MS = 100;

    public CmsServer server;
    public CmsClient client;

    /**
     * 检测端口是否可用（没有被占用）
     */
    private boolean isPortAvailable(int port) {
        try (ServerSocket testSocket = new ServerSocket(port)) {
            testSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void startServer() throws Exception {
        startServer(false);
    }

    public void startServer(boolean enableSecurity) throws Exception {
        server = new CmsServer(PORT);
        if (enableSecurity) {
            server.enableSecurity();
        }

        // 等待端口可用
        long startTime = System.currentTimeMillis();
        long deadline = startTime + MAX_WAIT_SECONDS * 1000L;

        while (!isPortAvailable(PORT)) {
            if (System.currentTimeMillis() > deadline) {
                throw new IOException("Port " + PORT + " still in use after " + MAX_WAIT_SECONDS + " seconds");
            }
            log.debug("Port {} is in use, waiting...", PORT);
            Thread.sleep(RETRY_INTERVAL_MS);
        }

        server.start();
        while (!server.isBound()) {
            Thread.sleep(10);
        }
        log.debug("Server started successfully on port {}", PORT);
    }

    public void startClient() throws Exception {
        startClient(false);
    }

    public void startClient(boolean enableSecurity) throws Exception {
        client = new CmsClient();
        if (enableSecurity) {
            client.enableSecurity();
        }
        client.setAccessPoint("IED1", "AP1");
        client.connect("127.0.0.1", PORT);
    }

    public void closeServer() {
        closeServer(500); // 默认等待500ms让socket释放（Windows需要更长时间）
    }

    public void closeServer(int waitMs) {
        if (server != null) {
            server.stop();
            server = null;
            if (waitMs > 0) {
                try {
                    Thread.sleep(waitMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void closeClient() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    public CmsApdu associate() throws Exception {
        CmsApdu response = client.associate();
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        return response;
    }
}
