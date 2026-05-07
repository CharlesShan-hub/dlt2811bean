package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.security.GmAuthenticator;
import com.ysh.dlt2811bean.security.GmSignature;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.security.GmTrustManager;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import com.ysh.dlt2811bean.transport.io.CmsServerTransport;
import com.ysh.dlt2811bean.transport.io.CmsTransportListener;
import com.ysh.dlt2811bean.transport.protocol.CmsDispatcher;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.protocol.association.AbortHandler;
import com.ysh.dlt2811bean.transport.protocol.association.AssociateHandler;
import com.ysh.dlt2811bean.transport.protocol.association.ReleaseHandler;
import com.ysh.dlt2811bean.transport.protocol.test.TestHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

import java.io.IOException;
import java.security.KeyPair;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CMS Server — Application layer entry point.
 *
 * <p>Owns the transport, protocol dispatcher, and manages all client sessions.
 * Provides a simple server lifecycle: start, stop.
 *
 * <p>Default handlers (SC=1,2,3,153) are automatically registered on {@link #start()}.
 *
 * <p>Example:
 * <pre>
 * CmsServer server = new CmsServer(8888);
 * server.start();  // handlers auto-registered
 * </pre>
 *
 * <p>With GM security:
 * <pre>
 * CmsServer server = new CmsServer(8888);
 * server.enableSecurity();  // 启用国密认证
 * server.start();
 * </pre>
 */
public class CmsServer {

    private static final Logger log = LoggerFactory.getLogger(CmsServer.class);

    private final CmsServerTransport transport;
    private final CmsDispatcher dispatcher;
    private final ConcurrentHashMap<CmsConnection, CmsServerSession> sessions = new ConcurrentHashMap<>();

    // ==================== Security (GM) ====================

    private boolean securityEnabled = false;
    private KeyPair securityKeyPair;
    private GmAuthenticator securityAuthenticator;
    private AssociateHandler associateHandler;

    /**
     * Creates a server listening on the given port.
     *
     * @param port the port to listen on
     */
    public CmsServer(int port) {
        this.transport = new CmsServerTransport(port, new ServerListener());
        this.dispatcher = new CmsDispatcher();
    }

    // ==================== Lifecycle ====================

    /**
     * Starts the server (starts accepting connections).
     * Automatically registers default handlers if not already registered.
     *
     * @throws IOException if the port cannot be bound
     */
    public void start() throws IOException {
        // Auto-register default handlers if not already registered
        registerDefaultHandlers();
        transport.start();
        log.info("CMS Server started on port {}", transport.getPort());
    }

    /**
     * Stops the server and closes all client sessions.
     */
    public void stop() {
        for (CmsServerSession session : sessions.values()) {
            session.getConnection().close();
        }
        sessions.clear();
        transport.stop();
        log.info("CMS Server stopped");
    }

    /**
     * Stops the server after a delay.
     *
     * @param delayMs milliseconds to wait before stopping
     * @throws InterruptedException if the delay is interrupted
     */
    public void stop(long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        stop();
    }

    /**
     * @return true if the server socket is bound and listening
     */
    public boolean isBound() {
        return transport.isBound();
    }

    // ==================== TLS Config ====================

    /**
     * Sets the 国密 SSL context for TLS connections.
     * Must be called before {@link #start()}.
     *
     * @param sslContext the SSL context
     * @return this server for chaining
     */
    public CmsServer sslContext(GmSslContext sslContext) {
        transport.sslContext(sslContext);
        return this;
    }

    /**
     * Sets whether client certificate is required (mutual TLS).
     * Must be called before {@link #start()}.
     *
     * @param need true to require client certificate
     * @return this server for chaining
     */
    public CmsServer needClientAuth(boolean need) {
        transport.needClientAuth(need);
        return this;
    }

    // ==================== Security (GM) ====================

    /**
     * Enables GM (Guomi) security for this server.
     * When enabled, AssociateHandler will verify client authentication certificates.
     *
     * <p>This method:
     * <ul>
     *   <li>Generates a SM2 key pair for the server</li>
     *   <li>Creates a trust manager that trusts all client certificates</li>
     *   <li>Creates an authenticator for certificate verification</li>
     * </ul>
     *
     * <p>Call this method before {@link #start()}.
     *
     * @return this server for chaining
     * @throws Exception if key pair generation fails
     */
    public CmsServer enableSecurity() throws Exception {
        // Generate SM2 key pair for the server
        this.securityKeyPair = GmSignature.generateKeyPair();

        // Create trust manager that trusts all (for receiving client certificates)
        // 默认启用 trustAll 模式，简化开发/测试
        GmTrustManager trustManager = new GmTrustManager().trustAll();

        // Create authenticator for verification
        this.securityAuthenticator = new GmAuthenticator(trustManager);

        // Create associate handler with authenticator and require authentication
        this.associateHandler = new AssociateHandler().enableSecurity(securityAuthenticator);

        this.securityEnabled = true;
        log.info("GM security enabled, server key pair generated");
        return this;
    }

    /**
     * Checks if GM security is enabled for this server.
     *
     * @return true if security is enabled
     */
    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    // ==================== Handlers ====================

    /**
     * Registers default association service handlers: Associate, Release, Abort, and Test.
     * These are the basic services required for connection lifecycle management.
     *
     * <p>Automatically called by {@link #start()} if not already registered.
     * Can be called manually to register additional handlers before default ones.
     * Safe to call multiple times (idempotent).
     *
     * <p>Registered handlers:
     * <ul>
     *   <li>SC=1  Associate  — generates association ID</li>
     *   <li>SC=2  Abort      — one-way, clears association</li>
     *   <li>SC=3  Release    — responds positive, clears association</li>
     *   <li>SC=153 Test      — echoes the frame</li>
     * </ul>
     *
     * @return this server for chaining
     */
    public CmsServer registerDefaultHandlers() {
        // Use associate handler with security if enabled, otherwise create new one
        if (associateHandler != null) {
            dispatcher.registerHandler(associateHandler);
        } else if (!dispatcher.hasHandler(ServiceName.ASSOCIATE)) {
            dispatcher.registerHandler(new AssociateHandler());
        }
        if (!dispatcher.hasHandler(ServiceName.ABORT)) {
            dispatcher.registerHandler(new AbortHandler());
        }
        if (!dispatcher.hasHandler(ServiceName.RELEASE)) {
            dispatcher.registerHandler(new ReleaseHandler());
        }
        if (!dispatcher.hasHandler(ServiceName.TEST)) {
            dispatcher.registerHandler(new TestHandler());
        }
        return this;
    }

    /**
     * Registers a service handler.
     *
     * @param handler the handler to register
     */
    public void registerHandler(CmsServiceHandler handler) {
        dispatcher.registerHandler(handler);
    }

    // ==================== Transport Listener ====================

    private class ServerListener implements CmsTransportListener {

        @Override
        public void onConnected(CmsConnection conn) {
            CmsServerSession session = new CmsServerSession(conn);
            sessions.put(conn, session);
            log.debug("[{}] Client connected from {}", session, session.getClientAddress());
        }

        @Override
        public void onApduReceived(CmsConnection conn, CmsApdu apdu) {
            CmsServerSession session = sessions.get(conn);
            if (session == null) {
                log.warn("Received APDU for unknown connection {}", conn);
                return;
            }

            try {
                CmsApdu response = dispatcher.dispatch(session, apdu);
                if (response != null) {
                    conn.send(response);
                }
            } catch (Exception e) {
                log.error("[{}] Error dispatching APDU: {}", session, e.getMessage(), e);
            }
        }

        @Override
        public void onDisconnected(CmsConnection conn) {
            CmsServerSession session = sessions.remove(conn);
            if (session != null) {
                session.onDisconnected();
                log.debug("[{}] Client disconnected", session);
            }
        }

        @Override
        public void onError(CmsConnection conn, Exception e) {
            log.error("Transport error on {}: {}", conn, e.getMessage(), e);
        }
    }
}
