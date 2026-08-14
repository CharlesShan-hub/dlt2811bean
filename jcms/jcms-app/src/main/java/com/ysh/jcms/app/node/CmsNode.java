package com.ysh.jcms.app.node;

import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.security.GmCredentialManager;
import com.ysh.jcms.utils.security.SecurityContext;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.core.data.sequence.block.CmsSgcb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CmsNode {

    private static final Logger log = LoggerFactory.getLogger(CmsNode.class);

    /* ====== fields ====== */

    private final InnerServer server;
    private final InnerClient client = new InnerClient();
    private final Map<Class<?>, Object> clientHandlers = new HashMap<>();
    private final SclManager sclManager = new SclManager();
    private final ContentManager contentManager = new ContentManager();
    private final GmCredentialManager credentialManager;

    /* ====== constructors ====== */

    public CmsNode(boolean createServer) {
        this.server = createServer ? new InnerServer() : null;
        this.credentialManager = initCredentialManager();
    }

    public CmsNode(int serverPort) {
        this.server = serverPort > 0 ? new InnerServer(serverPort, 0) : null;
        this.credentialManager = initCredentialManager();
    }

    /* ====== private helpers ====== */

    private static GmCredentialManager initCredentialManager() {
        try {
            return SecurityContext.generateSelfSigned().credentialManager();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize GmCredentialManager", e);
        }
    }

    /* ====== registration ====== */

    public void registerServer(ServiceHandler handler) {
        if (server != null)
            server.register(handler);
    }

    public void registerClient(BaseClientHandler handler) {
        handler.node(this);
        clientHandlers.put(handler.getClass(), handler);
    }

    @SuppressWarnings("unchecked")
    public <T> T getClient(Class<T> type) {
        return (T) clientHandlers.get(type);
    }

    /* ====== lifecycle ====== */

    /**
     * Start the server node.
     *
     * @param test
     *            if {@code true}, load SCL from {@code testSclFiles}
     */
    public void start(boolean test) throws IOException {
        if (server != null) {
            CmsConfig.Server cfg = CmsConfigLoader.load().server();
            log.info("SCL config: sclFiles={}, testSclFiles={}", cfg.sclFiles(), cfg.testSclFiles());
            String sclFile = test ? cfg.getResolvedTestSclFile() : cfg.getResolvedSclFile();
            log.info("SCL file resolved: {} (test={})", sclFile, test);
            if (sclFile != null) {
                sclManager.load(sclFile);
                if (sclManager.loaded()) {
                    SclDocument sclDoc = sclManager.document();
                    server.sclDocument(sclDoc);
                    if (sclDoc != null) {
                        new ReportEngine(sclDoc);
                        log.info("ReportEngine initialized with SCL document");
                        initSgcb(sclDoc);
                    } else {
                        log.warn("No SCL document available - ReportEngine not initialized");
                    }
                }
            }
            server.start();
        }
    }

    public void stop() {
        client.close();
        if (server != null)
            server.stop();
    }

    /* ====== client operations ====== */

    public void connect(String host, int port) throws IOException {
        client.connect(host, port);
    }

    public void connectTls(String host, int port, SSLContext sslContext) throws IOException {
        client.connectTls(host, port, sslContext);
    }

    public Frame sendRequest(CmsServiceInfo sc, byte[] asduBytes, long timeoutMs) throws IOException {
        return client.sendRequest(sc, asduBytes, timeoutMs);
    }

    public Frame sendRequest(CmsServiceInfo sc, byte[] asduBytes) throws IOException {
        return client.sendRequest(sc, asduBytes);
    }

    public void close() {
        client.close();
    }

    // ==================== SGCB initialization ====================

    private static void initSgcb(SclDocument sclDoc) {
        int numOfSG = CmsConfigLoader.load().protocol().setting().numOfSG();
        for (SclIED ied : sclDoc.ieds()) {
            for (SclLDevice ld : ied.lDevices()) {
                for (SclLN ln : ld.findLnsByClass("LLN0")) {
                    String prefix = ld.inst() + "/" + ln.getFullName() + ".SG";
                    for (int i = 1; i <= numOfSG; i++) {
                        CmsSgcb sgcb = new CmsSgcb().numOfSG(numOfSG).actSG(1).editSG(1);
                        sgcb.tActEdt.now();
                        sgcb.setPresent("resvTms", false);
                        CbStateManager.SGCB.put(prefix + i, sgcb);
                    }
                }
            }
        }
        log.info("SGCB entries initialized: numOfSG={}, total={}", numOfSG, CbStateManager.SGCB.size());
    }

    /**
     * Execute a registered client handler via reflection.
     *
     * <pre>
     * CmsAssociateResponse resp = node.execute(AssociateClient.class, dao);
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public <T> T execute(Class<?> handlerClass, Object... args) throws Exception {
        Object handler = clientHandlers.get(handlerClass);
        if (handler == null) {
            throw new IllegalStateException("No handler registered for " + handlerClass.getSimpleName());
        }
        for (Method m : handler.getClass().getMethods()) {
            if (m.getName().equals("execute") && m.getParameterCount() == args.length) {
                try {
                    return (T) m.invoke(handler, args);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    if (e.getCause() instanceof Exception) {
                        throw (Exception) e.getCause();
                    }
                    throw e;
                }
            }
        }
        throw new IllegalStateException("No execute method with " + args.length + " params on " + handlerClass.getSimpleName());
    }

    /* ====== accessors ====== */

    public SclManager sclManager() {
        return sclManager;
    }
    public ContentManager contentManager() {
        return contentManager;
    }
    public GmCredentialManager credentialManager() {
        return credentialManager;
    }
    public boolean clientConnected() {
        return client.connected();
    }
    public boolean serverRunning() {
        return server != null && server.running();
    }
    public InnerServer server() {
        return server;
    }
    public InnerClient client() {
        return client;
    }
}
