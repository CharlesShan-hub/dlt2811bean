package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import com.ysh.dlt2811bean.transport.io.CmsServerTransport;
import com.ysh.dlt2811bean.transport.io.CmsTransportListener;
import com.ysh.dlt2811bean.transport.protocol.CmsDispatcher;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
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
 * <p>Example:
 * <pre>
 * CmsServer server = new CmsServer(8888);
 * server.registerHandler(new AssociateHandler());
 * server.registerHandler(new ReleaseHandler());
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

    // ==================== Handlers ====================

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
