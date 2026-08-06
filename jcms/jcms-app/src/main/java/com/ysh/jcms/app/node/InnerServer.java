package com.ysh.jcms.app.node;

import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.service.Dispatcher;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;
import com.ysh.jcms.utils.transport.wire.Connection;
import com.ysh.jcms.utils.transport.wire.ConnectionListener;
import com.ysh.jcms.utils.transport.wire.ServerAcceptor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class InnerServer implements ConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(InnerServer.class);

    private final int port;
    private final int sslPort;
    private final ServerAcceptor acceptor;
    private ServerAcceptor sslAcceptor;
    private final Dispatcher dispatcher = new Dispatcher();
    private final CopyOnWriteArrayList<ServerSession> sessions = new CopyOnWriteArrayList<>();
    private SclDocument scl2Document;
    private KeepAliveManager keepalive;

    public InnerServer() {
        CmsConfig.Server cfg = CmsConfigLoader.load().server();
        this.port = cfg.port();
        this.sslPort = cfg.sslPort();
        this.acceptor = new ServerAcceptor(port, this);
        configureTls();
    }

    public InnerServer(int port, int sslPort) {
        this.port = port;
        this.sslPort = sslPort;
        this.acceptor = new ServerAcceptor(port, this);
    }

    private void configureTls() {
        if (sslPort <= 0)
            return;
        try {
            KeyPair kp = generateRsaKeyPair();
            X509Certificate cert = generateSelfSignedCert(kp);
            SSLContext ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(createKeyManagers(kp, cert), new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            this.sslAcceptor = new ServerAcceptor(sslPort, this).sslContext(ctx).needClientAuth(false);
        } catch (Exception e) {
            log.warn("Failed to configure TLS on port {}: {}", sslPort, e.getMessage());
        }
    }

    private static KeyPair generateRsaKeyPair() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048, new SecureRandom());
        return gen.generateKeyPair();
    }

    private static X509Certificate generateSelfSignedCert(KeyPair kp) throws Exception {
        long now = System.currentTimeMillis();
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        X500Name name = new X500Name("CN=CMS Dev Server");
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(name, BigInteger.valueOf(now), new Date(now),
                new Date(now + 365L * 24 * 60 * 60 * 1000), name, kp.getPublic());
        return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(builder.build(
                new JcaContentSignerBuilder("SHA256WithRSA").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(kp.getPrivate())));
    }

    private static KeyManager[] createKeyManagers(KeyPair kp, X509Certificate cert) throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setKeyEntry("key", kp.getPrivate(), "".toCharArray(), new X509Certificate[]{cert});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, "".toCharArray());
        return kmf.getKeyManagers();
    }

    public SclDocument getSclDocument() {
        return scl2Document;
    }
    public void setScl2Document(SclDocument doc) {
        this.scl2Document = doc;
    }

    public void register(ServiceHandler handler) {
        dispatcher.register(handler);
    }

    public void start() throws IOException {
        acceptor.start();
        if (sslAcceptor != null)
            sslAcceptor.start();
        this.keepalive = new KeepAliveManager(sessions);
        keepalive.start();
        log.info("InnerServer started on port {} ({}{})", port, sslAcceptor != null ? "TLS: " : "",
                sslAcceptor != null ? String.valueOf(sslPort) : "");
    }

    public void stop() {
        if (keepalive != null) {
            keepalive.stop();
            keepalive = null;
        }
        acceptor.stop();
        if (sslAcceptor != null) {
            sslAcceptor.stop();
            sslAcceptor = null;
        }
        for (ServerSession ss : sessions)
            ss.close();
        sessions.clear();
    }

    public int getPort() {
        return port;
    }
    public int getSslPort() {
        return sslPort;
    }
    public boolean hasTls() {
        return sslAcceptor != null;
    }
    public boolean isRunning() {
        return acceptor.isRunning();
    }
    public java.util.List<ServerSession> getSessions() {
        return new java.util.ArrayList<>(sessions);
    }

    @Override
    public void onConnected(Connection connection) {
        ServerSession ss = new ServerSession(connection);
        ss.setScl2Document(scl2Document);
        ss.touchActivity();
        sessions.add(ss);
    }

    @Override
    public void onFrameReceived(Connection connection, Frame frame) {
        ServerSession ss = findSession(connection);
        if (ss == null)
            return;
        ss.touchActivity();
        Dispatcher.DispatchOutcome outcome = dispatcher.dispatch(ss, frame);
        switch (outcome.getResult()) {
            case HANDLED :
                if (outcome.getResponse() != null) {
                    try {
                        connection.send(outcome.getResponse());
                    } catch (IOException e) {
                        log.error("Send response failed", e);
                    }
                }
                break;
            case NOT_REGISTERED :
                log.warn("No handler for service: {}", frame.header().serviceCode());
                try {
                    connection.send(new Frame(new FrameHeader().serviceCode(frame.header().serviceCode()).resp(true).err(true),
                            new byte[]{0, 0}, frame.reqId()));
                } catch (IOException e) {
                    log.error("Failed to send NOT_REGISTERED error", e);
                }
                break;
            case ERROR_OCCURRED :
                log.error("Handler error for service: {}", frame.header().serviceCode());
                break;
        }
    }

    @Override
    public void onDisconnected(Connection connection) {
        ServerSession ss = findSession(connection);
        if (ss != null) {
            ss.setState(SessionState.DISCONNECTED);
            sessions.remove(ss);
        }
    }

    @Override
    public void onError(Connection connection, Exception e) {
        log.error("Connection error", e);
    }

    private ServerSession findSession(Connection connection) {
        for (ServerSession ss : sessions) {
            if (ss.getConnection() == connection)
                return ss;
        }
        return null;
    }

    public static class ServerSession extends Session {
        private SclDocument scl2Document;
        private SclAccessPoint sclAccessPoint;
        private SclIED sclIed;
        private SclDataTypeTemplates sclDataTypeTemplates;
        private volatile long lastActivityTime = System.currentTimeMillis();
        private volatile int keepaliveRetries;

        public ServerSession(Connection connection) {
            super("srv-" + connection.getSocket().getPort(), connection);
        }

        public void touchActivity() {
            this.lastActivityTime = System.currentTimeMillis();
            this.keepaliveRetries = 0;
        }
        public long getLastActivityTime() {
            return lastActivityTime;
        }
        public int getKeepaliveRetries() {
            return keepaliveRetries;
        }
        public int incrementKeepaliveRetries() {
            return ++keepaliveRetries;
        }
        public void close() {
            getConnection().close();
        }

        public SclDocument getSclDocument() {
            return scl2Document;
        }
        public void setScl2Document(SclDocument doc) {
            this.scl2Document = doc;
        }
        public SclAccessPoint getSclAccessPoint() {
            return sclAccessPoint;
        }
        public void setSclAccessPoint(SclAccessPoint sclAccessPoint) {
            this.sclAccessPoint = sclAccessPoint;
        }

        public SclIED getSclIed() {
            return sclIed;
        }
        public void setSclIed(SclIED ied) {
            this.sclIed = ied;
        }
        public SclDataTypeTemplates getSclDataTypeTemplates() {
            return sclDataTypeTemplates;
        }
        public void setSclDataTypeTemplates(SclDataTypeTemplates sclDataTypeTemplates) {
            this.sclDataTypeTemplates = sclDataTypeTemplates;
        }

        @Override
        public void clear() {
            SgSessionState.clear(getSessionId());
            super.clear();
        }
    }
}
