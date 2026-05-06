package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.security.GmSslContext;
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

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CMS Server — Application layer entry point.
 *
 * <p>Owns the transport, protocol dispatcher, and manages all client sessions.
 * Provides a simple server lifecycle: start, stop, register handlers.
 *
 * <p>Default handlers (SC=1,2,3,153) are registered via {@link #registerDefaultHandlers()}.
 *
 * <p>Example:
 * <pre>
 * CmsServer server = new CmsServer(8888);
 * server.registerDefaultHandlers();  // Associate, Release, Abort, Test
 * // register additional handlers for other services...
 * server.start();
 * </pre>
 */
public class CmsServer {

    private static final Logger log = LoggerFactory.getLogger(CmsServer.class);

    private final CmsServerTransport transport;
    private final CmsDispatcher dispatcher;
    private final ConcurrentHashMap<CmsConnection, CmsServerSession> sessions = new ConcurrentHashMap<>();

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
     *
     * @throws IOException if the port cannot be bound
     */
    public void start() throws IOException {
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

    // ==================== Handlers ====================

    /**
     * Registers default association service handlers: Associate, Release, Abort, and Test.
     * These are the basic services required for connection lifecycle management.
     *
     * <p>Call this method before {@link #start()}.
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
        dispatcher.registerHandler(new AssociateHandler());
        dispatcher.registerHandler(new AbortHandler());
        dispatcher.registerHandler(new ReleaseHandler());
        dispatcher.registerHandler(new TestHandler());
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
