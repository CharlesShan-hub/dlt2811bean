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
 *
 * <p>Provides automatic lifecycle management via {@link #useAutoLifecycle()}.
 * Subclasses that override {@code useAutoLifecycle()} to return {@code false}
 * must manually manage server/client lifecycle in their own test methods.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoopbackTest {

    protected static final Logger log = LoggerFactory.getLogger(LoopbackTest.class);

    protected static final int PORT = 18773;
    private static final int MAX_WAIT_SECONDS = 30;
    private static final int RETRY_INTERVAL_MS = 100;

    public CmsServer server;
    public CmsClient client;

    /**
     * Whether to use automatic {@code @BeforeAll}/{code @AfterAll}/{code @BeforeEach}/{code @AfterEach} lifecycle.
     * Override to return {@code false} in subclasses that need manual control
     * (e.g. when different test methods require different server/client configurations).
     */
    protected boolean useAutoLifecycle() {
        return true;
    }

    @BeforeAll
    void beforeAll() throws Exception {
        if (!useAutoLifecycle()) {
            return;
        }
        startServer(false);
    }

    @AfterAll
    void afterAll() throws Exception {
        if (!useAutoLifecycle()) {
            return;
        }
        closeServer();
    }

    @BeforeEach
    void setUp() throws Exception {
        if (!useAutoLifecycle()) {
            return;
        }
        startClient(false);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (!useAutoLifecycle()) {
            return;
        }
        closeClient();
    }

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

    private static final String SCL_FILE = "config/sample-scd-full.scd";

    public void startServer(boolean enableSecurity) throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
        server = new CmsServer(PORT, SCL_FILE);
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
            //log.debug("Port {} is in use, waiting...", PORT);
            Thread.sleep(RETRY_INTERVAL_MS);
        }

        server.start();
        while (!server.isBound()) {
            Thread.sleep(10);
        }
        //log.debug("Server started successfully on port {}", PORT);
    }

    public void startClient() throws Exception {
        startClient(false);
    }

    public void startClient(boolean enableSecurity) throws Exception {
        client = new CmsClient();
        if (enableSecurity) {
            client.enableSecurity();
        }
        client.setAccessPoint("E1Q1SB1", "S1");
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

    public CmsApdu associate(String ap, String ep) throws Exception {
        CmsApdu response = client.associate(ap, ep);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        return response;
    }
}
