package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.scl2.model.SclDocument;
import com.ysh.dlt2811bean.scl2.model.SclIED;
import com.ysh.dlt2811bean.scl2.reader.SclReader;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.security.GmAuthenticator;
import com.ysh.dlt2811bean.security.GmSignature;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.security.GmTrustManager;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCommandTermination;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperateTermination;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsReportEntryData;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import com.ysh.dlt2811bean.transport.io.CmsServerTransport;
import com.ysh.dlt2811bean.transport.io.CmsTransportListener;
import com.ysh.dlt2811bean.transport.protocol.CmsDispatcher;
import com.ysh.dlt2811bean.transport.protocol.association.*;
import com.ysh.dlt2811bean.transport.protocol.directory.*;
import com.ysh.dlt2811bean.transport.protocol.test.*;
// import com.ysh.dlt2811bean.transport.protocol.control.*;
import com.ysh.dlt2811bean.transport.protocol.data.*;
// import com.ysh.dlt2811bean.transport.protocol.sv.*;
import com.ysh.dlt2811bean.transport.goose.GoosePublisher;
// import com.ysh.dlt2811bean.transport.protocol.goose.*;
// import com.ysh.dlt2811bean.transport.protocol.log.*;
// import com.ysh.dlt2811bean.transport.protocol.report.*;
// import com.ysh.dlt2811bean.transport.protocol.setting.*;
import com.ysh.dlt2811bean.transport.protocol.dataset.*;
// import com.ysh.dlt2811bean.transport.protocol.file.*;
import com.ysh.dlt2811bean.transport.protocol.negotiation.*;
// import com.ysh.dlt2811bean.transport.protocol.rpc.*;
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
    private CmsServerTransport tlsTransport;
    private final CmsDispatcher dispatcher;
    private final ConcurrentHashMap<CmsConnection, CmsServerSession> sessions = new ConcurrentHashMap<>();
    private final GoosePublisher goosePublisher;
    private String sclFilePath;
    private SclDocument sclDocument;
    private boolean securityEnabled = false;
    private GmAuthenticator securityAuthenticator;
    private X509Certificate serverCertificate;

    public CmsServer() {
        CmsConfig config = CmsConfigLoader.load();
        this.transport = new CmsServerTransport(config.getServer().getPort(), new ServerListener());
        this.dispatcher = new CmsDispatcher();
        this.goosePublisher = new GoosePublisher();
        loadSclSilently(config.getServer().getSclFile());
    }

    private void loadSclSilently(String sclPath) {
        try {
            loadScl(sclPath);
        } catch (Exception e) {
            log.warn("Failed to load SCL from {}: {}", sclPath, e.getMessage());
        }
    }

    public CmsServer(int port) {
        this.transport = new CmsServerTransport(port, new ServerListener());
        this.dispatcher = new CmsDispatcher();
        this.goosePublisher = new GoosePublisher();
    }

    public CmsServer(int port, String sclPath) throws Exception {
        this(port);
        loadScl(sclPath);
    }

    public CmsServer(int port, Path sclPath) throws Exception {
        this(port);
        loadScl(sclPath);
    }

    public CmsServer(String sclPath) throws Exception {
        this();
        loadScl(sclPath);
    }

    // ==================== Lifecycle ====================

    public void start() throws IOException {
        registerDefaultHandlers();
        try {
            goosePublisher.init();
            goosePublisher.dataProvider((goCBRef, dataSetRefs) -> null);
        } catch (Exception e) {
            log.warn("GoosePublisher init failed (GOOSE publishing unavailable): {}", e.getMessage());
        }
        transport.start();
        log.info("CMS Server started on port {}", transport.getPort());
        if (tlsTransport != null) {
            tlsTransport.start();
            log.info("CMS Server started TLS on port {}", tlsTransport.getPort());
        }
    }

    public void stop() {
        goosePublisher.destroy();
        for (CmsServerSession session : sessions.values()) {
            session.getConnection().close();
        }
        sessions.clear();
        transport.stop();
        if (tlsTransport != null) {
            tlsTransport.stop();
        }
        log.info("CMS Server stopped");
    }

    public void stop(long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        stop();
    }

    public boolean isBound() {
        return transport.isBound();
    }

    public int getPort() {
        return transport.getPort();
    }

    // ==================== TLS Config ====================

    public CmsServer sslContext(GmSslContext sslContext) {
        CmsConfig config = CmsConfigLoader.load();
        this.tlsTransport = new CmsServerTransport(config.getServer().getSslPort(), new ServerListener());
        this.tlsTransport.sslContext(sslContext);
        return this;
    }

    public CmsServer sslContext(GmSslContext sslContext, int tlsPort) {
        this.tlsTransport = new CmsServerTransport(tlsPort, new ServerListener());
        this.tlsTransport.sslContext(sslContext);
        return this;
    }

    public int getTlsPort() {
        return tlsTransport != null ? tlsTransport.getPort() : -1;
    }

    public CmsServer needClientAuth(boolean need) {
        if (tlsTransport != null) {
            tlsTransport.needClientAuth(need);
        }
        return this;
    }

    // ==================== SCL Model ====================

    public CmsServer loadScl(String filePath) throws Exception {
        this.sclFilePath = filePath;
        this.sclDocument = new SclReader().read(new java.io.File(filePath));
        log.info("SCL model loaded from {}: type={}, IEDs={}", filePath,
                 sclDocument.getFileType(), sclDocument.getIeds().size());
        reRegisterSclHandlers();
        printConnectHint();
        return this;
    }

    private void printConnectHint() {
        if (sclDocument == null || sclDocument.getIeds().isEmpty()) return;
        CmsConfig config = CmsConfigLoader.load();
        int port = config.getServer().getPort();
        int asduSize = config.getNegotiate().getAsduSize();
        long protoVer = config.getNegotiate().getProtocolVersion();

        System.out.println(CmsColor.gray("\n  " + CmsColor.bold("Client connect command:")));
        for (SclIED ied : sclDocument.getIeds()) {
            for (var accPt : ied.getAccessPoints()) {
                String iedAp = ied.getName();
                String accPtName = accPt.getName();
                System.out.println(CmsColor.gray("    connect 127.0.0.1 " + port + " " + asduSize + " " + protoVer + " " + iedAp + " " + accPtName));
            }
        }
        System.out.println(CmsColor.gray(""));
    }

    private void reRegisterSclHandlers() throws Exception {
        if (securityEnabled && securityAuthenticator != null && serverCertificate != null) {
            dispatcher.registerHandler(
                new AssociateHandler().enableSecurity(securityAuthenticator, serverCertificate));
        } else {
            dispatcher.registerHandler(new AssociateHandler());
        }
        log.info("SCL-dependent handlers re-registered");
    }

    /**
     * Reloads the previously loaded SCL file.
     */
    public CmsServer reloadScl() throws Exception {
        if (sclFilePath == null) {
            throw new IllegalStateException("No SCL file previously loaded");
        }
        return loadScl(sclFilePath);
    }

    public String getSclFilePath() {
        return sclFilePath;
    }

    public CmsServer loadScl(Path filePath) throws Exception {
        this.sclDocument = new SclReader().read(new java.io.File(filePath.toString()));
        this.sclFilePath = filePath.toString();
        log.info("SCL model loaded from {}: type={}, IEDs={}", filePath,
                 sclDocument.getFileType(), sclDocument.getIeds().size());
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
        this.serverCertificate = GmSignature.generateSelfSignedCertificate(keyPair);

        GmTrustManager trustManager = new GmTrustManager().trustAll();
        this.securityAuthenticator = new GmAuthenticator(trustManager);

        dispatcher.registerHandler(
            new AssociateHandler().enableSecurity(securityAuthenticator, serverCertificate));

        this.securityEnabled = true;
        log.info("GM security enabled");
        return this;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    public GoosePublisher getGoosePublisher() {
        return goosePublisher;
    }

    // ==================== Session Access ====================

    /**
     * Returns all active server sessions.
     */
    public java.util.Collection<CmsServerSession> getSessions() {
        return sessions.values();
    }

    // ==================== Push API (Server → Client) ====================

    /**
     * Pushes a CommandTermination notification to a specific client session.
     */
    public void pushCommandTermination(CmsServerSession session, String ref, CmsType<?> ctlVal) throws Exception {
        CmsCommandTermination asdu = new CmsCommandTermination(MessageType.REQUEST_POSITIVE)
                .reference(ref)
                .ctlVal(ctlVal)
                .ctlNum(1)
                .test(false);
        session.send(new CmsApdu(asdu));
        log.debug("[Server] Push CommandTermination: {}", ref);
    }

    /**
     * Pushes a TimeActivatedOperateTermination notification to a specific client session.
     */
    public void pushTimeActivatedOperateTermination(CmsServerSession session, String ref, CmsType<?> ctlVal) throws Exception {
        CmsTimeActivatedOperateTermination asdu = new CmsTimeActivatedOperateTermination(MessageType.REQUEST_POSITIVE)
                .reference(ref)
                .ctlVal(ctlVal)
                .ctlNum(1)
                .test(false);
        session.send(new CmsApdu(asdu));
        log.debug("[Server] Push TimeActivatedOperateTermination: {}", ref);
    }

    /**
     * Pushes a Report to a specific client session.
     */
    public void pushReport(CmsServerSession session, String rptID, String dataRef, String fc, int intVal) throws Exception {
        CmsReport report = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .rptID(rptID)
                .sqNum(1);
        CmsReportEntryData entryData = new CmsReportEntryData()
                .reference(dataRef)
                .fc(fc)
                .id(1)
                .value(new com.ysh.dlt2811bean.datatypes.numeric.CmsInt32(intVal));
        report.entry.entryData().add(entryData);
        session.send(new CmsApdu(report));
        log.debug("[Server] Push Report: {} -> {}", rptID, dataRef);
    }

    /**
     * Pushes a Report to a specific client session using a pre-built CmsReport ASDU.
     */
    public void pushReport(CmsServerSession session, CmsReport asdu) throws Exception {
        session.send(new CmsApdu(asdu));
        log.debug("[Server] Push Report: {}", asdu.rptID.get());
    }

    // ==================== Handlers ====================

    private void registerDefaultHandlers() {
        CmsConfig config = CmsConfigLoader.load();
        CmsConfig.Negotiate negCfg = config.getNegotiate();
        
        // 8.2 association handlers
        dispatcher.registerDefaultHandler(new AssociateHandler());// 8.2.1
        dispatcher.registerDefaultHandler(new AbortHandler());// 8.2.2
        dispatcher.registerDefaultHandler(new ReleaseHandler());// 8.2.3
        // 8.3 directory handlers
        dispatcher.registerDefaultHandler(new GetServerDirectoryHandler());// 8.3.1
        dispatcher.registerDefaultHandler(new GetLogicalDeviceDirectoryHandler());// 8.3.2
        dispatcher.registerDefaultHandler(new GetLogicalNodeDirectoryHandler());// 8.3.3
        dispatcher.registerDefaultHandler(new GetAllDataValuesHandler());// 8.3.4
        dispatcher.registerDefaultHandler(new GetAllDataDefinitionHandler());// 8.3.5
        dispatcher.registerDefaultHandler(new GetAllCBValuesHandler());// 8.3.6
        // 8.4 data handlers
        dispatcher.registerDefaultHandler(new GetDataValuesHandler());// 8.4.1
        dispatcher.registerDefaultHandler(new SetDataValuesHandler());// 8.4.2
        dispatcher.registerDefaultHandler(new GetDataDirectoryHandler());// 8.4.3
        dispatcher.registerDefaultHandler(new GetDataDefinitionHandler());// 8.4.4
        // 8.5 dataset handlers
        dispatcher.registerDefaultHandler(new GetDataSetValuesHandler());// 8.5.1
        dispatcher.registerDefaultHandler(new SetDataSetValuesHandler());// 8.5.2
        dispatcher.registerDefaultHandler(new CreateDataSetHandler());// 8.5.3
        dispatcher.registerDefaultHandler(new DeleteDataSetHandler());// 8.5.4
        dispatcher.registerDefaultHandler(new GetDataSetDirectoryHandler());// 8.5.5
        // 8.6 value handlers
        // dispatcher.registerDefaultHandler(new SelectActiveSGHandler());// 8.6.1
        // dispatcher.registerDefaultHandler(new SelectEditSGHandler());// 8.6.2
        // dispatcher.registerDefaultHandler(new SetEditSGValueHandler());// 8.6.3
        // dispatcher.registerDefaultHandler(new ConfirmEditSGValuesHandler());// 8.6.4
        // dispatcher.registerDefaultHandler(new GetEditSGValueHandler());// 8.6.5
        // dispatcher.registerDefaultHandler(new GetSGCBValuesHandler());// 8.6.6
        // 8.7 report handlers
        // dispatcher.registerDefaultHandler(new GetBRCBValuesHandler());// 8.7.2
        // dispatcher.registerDefaultHandler(new SetBRCBValuesHandler());// 8.7.3
        // dispatcher.registerDefaultHandler(new GetURCBValuesHandler());// 8.7.4
        // dispatcher.registerDefaultHandler(new SetURCBValuesHandler());// 8.7.5
        // 8.8 log handlers
        // dispatcher.registerDefaultHandler(new GetLCBValuesHandler());// 8.8.2
        // dispatcher.registerDefaultHandler(new SetLCBValuesHandler());// 8.8.3
        // dispatcher.registerDefaultHandler(new QueryLogByTimeHandler());// 8.8.4
        // dispatcher.registerDefaultHandler(new QueryLogAfterHandler());// 8.8.5
        // dispatcher.registerDefaultHandler(new GetLogStatusValuesHandler());// 8.8.6
        // 8.9 goose handlers
        // dispatcher.registerDefaultHandler(new GetGoCBValuesHandler());// 8.9.4
        // dispatcher.registerDefaultHandler(new SetGoCBValuesHandler(goosePublisher));// 8.9.5
        // 8.10 sv handlers
        // dispatcher.registerDefaultHandler(new GetMSVCBValuesHandler());// 8.10.2
        // dispatcher.registerDefaultHandler(new SetMSVCBValuesHandler());// 8.10.3
        // 8.11 control handlers
        // dispatcher.registerDefaultHandler(new SelectHandler());// 8.11.1
        // dispatcher.registerDefaultHandler(new SelectWithValueHandler());// 8.11.2
        // dispatcher.registerDefaultHandler(new OperateHandler());// 8.11.3
        // dispatcher.registerDefaultHandler(new CancelHandler());// 8.11.4
        // dispatcher.registerDefaultHandler(new TimeActivatedOperateHandler());// 8.11.6
        // 8.12 rpc handlers
        // dispatcher.registerDefaultHandler(new GetRpcInterfaceDirectoryHandler());// 8.12.1
        // dispatcher.registerDefaultHandler(new GetRpcInterfaceDefinitionHandler());// 8.12.2
        // dispatcher.registerDefaultHandler(new GetRpcMethodDirectoryHandler());// 8.12.3
        // dispatcher.registerDefaultHandler(new GetRpcMethodDefinitionHandler());// 8.12.4
        // dispatcher.registerDefaultHandler(new RpcCallHandler());// 8.12.5
        // 8.13 file handlers
        // dispatcher.registerDefaultHandler(new GetFileHandler());// 8.13.1
        // dispatcher.registerDefaultHandler(new SetFileHandler());// 8.13.2
        // dispatcher.registerDefaultHandler(new DeleteFileHandler());// 8.13.3
        // dispatcher.registerDefaultHandler(new GetFileAttributeValuesHandler());// 8.13.4
        // dispatcher.registerDefaultHandler(new GetFileDirectoryHandler());// 8.13.5
        // 8.14 test handlers
        dispatcher.registerDefaultHandler(new TestHandler());// 8.14.1
        // 8.15 negotiation handlers
        dispatcher.registerDefaultHandler(new AssociateNegotiateHandler(
                negCfg.getApduSize(), negCfg.getAsduSize(),
                negCfg.getProtocolVersion(), negCfg.getModelVersion()));// 8.15.1
    }

    // ==================== Transport Listener ====================

    private class ServerListener implements CmsTransportListener {

        @Override
        public void onConnected(CmsConnection conn) {
            CmsServerSession session = new CmsServerSession(conn);
            session.setSclDocument(sclDocument);
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

            session.onDataActivity();

            // Server only handles REQUESTs. RESPONSEs from client (e.g. push acknowledgments) are ignored.
            if (apdu.getApch().isResp()) {
                log.debug("[{}] Ignoring response APDU (SC=0x{:02X})", session, apdu.getApch().getServiceCode().getCode());
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

    public static void main(String[] args) throws Exception {
        CmsServer server = new CmsServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped");
        }));
        server.start();
        System.out.println("CMS Server running on port " + CmsConfigLoader.load().getServer().getPort() + "...");
        if (server.tlsTransport != null) {
            System.out.println("CMS TLS Server running on port " + CmsConfigLoader.load().getServer().getSslPort() + "...");
        }
        System.out.println("Press Ctrl+C to stop");
        Thread.currentThread().join();
    }
}
