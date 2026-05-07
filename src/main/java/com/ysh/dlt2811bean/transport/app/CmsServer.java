package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.SclReader;
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
import com.ysh.dlt2811bean.transport.protocol.directory.GetServerDirectoryHandler;
import com.ysh.dlt2811bean.transport.protocol.test.TestHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
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
    private SclDocument sclDocument;
    private boolean securityEnabled = false;

    public CmsServer(int port) {
        this.transport = new CmsServerTransport(port, new ServerListener());
        this.dispatcher = new CmsDispatcher();
    }

    public CmsServer(int port, String sclPath) throws Exception {
        this(port);
        loadScl(sclPath);
    }

    public CmsServer(int port, Path sclPath) throws Exception {
        this(port);
        loadScl(sclPath);
    }

    // ==================== Lifecycle ====================

    public void start() throws IOException {
        registerDefaultHandlers();
        transport.start();
        log.info("CMS Server started on port {}", transport.getPort());
    }

    public void stop() {
        for (CmsServerSession session : sessions.values()) {
            session.getConnection().close();
        }
        sessions.clear();
        transport.stop();
        log.info("CMS Server stopped");
    }

    public void stop(long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        stop();
    }

    public boolean isBound() {
        return transport.isBound();
    }

    // ==================== TLS Config ====================

    public CmsServer sslContext(GmSslContext sslContext) {
        transport.sslContext(sslContext);
        return this;
    }

    public CmsServer needClientAuth(boolean need) {
        transport.needClientAuth(need);
        return this;
    }

    // ==================== SCL Model ====================

    public CmsServer loadScl(String filePath) throws Exception {
        this.sclDocument = new SclReader().read(filePath);
        log.info("SCL model loaded from {}: IEDs={}", filePath,
                 sclDocument.getIeds().size());
        return this;
    }

    public CmsServer loadScl(Path filePath) throws Exception {
        this.sclDocument = new SclReader().read(filePath);
        log.info("SCL model loaded from {}: IEDs={}", filePath,
                 sclDocument.getIeds().size());
        return this;
    }

    public SclDocument getSclDocument() {
        return sclDocument;
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
        KeyPair keyPair = GmSignature.generateKeyPair();
        X509Certificate serverCert = GmSignature.generateSelfSignedCertificate(keyPair);

        GmTrustManager trustManager = new GmTrustManager().trustAll();
        GmAuthenticator authenticator = new GmAuthenticator(trustManager);

        dispatcher.registerHandler(
            new AssociateHandler(sclDocument).enableSecurity(authenticator, serverCert));

        this.securityEnabled = true;
        log.info("GM security enabled");
        return this;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    // ==================== Handlers ====================

    private void registerDefaultHandlers() {
        // associate handler
        dispatcher.registerDefaultHandler(new AssociateHandler(sclDocument));
        dispatcher.registerDefaultHandler(new AbortHandler());
        dispatcher.registerDefaultHandler(new ReleaseHandler());
        // directory handlers
        dispatcher.registerDefaultHandler(new GetServerDirectoryHandler());
        // test handlers
        dispatcher.registerDefaultHandler(new TestHandler());
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
