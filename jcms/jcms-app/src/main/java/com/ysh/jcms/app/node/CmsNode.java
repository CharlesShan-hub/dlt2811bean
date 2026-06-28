package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.security.GmCredentialManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CmsNode {

    private static final Logger log = LoggerFactory.getLogger(CmsNode.class);

    private final InnerServer server;
    private final InnerClient client = new InnerClient();
    private final Map<Class<?>, Object> clientHandlers = new HashMap<>();
    private final SclManager sclManager = new SclManager();
    private final ContentManager contentManager = new ContentManager();
    private GmCredentialManager credentialManager;

    public CmsNode(boolean createServer) {
        this.server = createServer ? new InnerServer() : null;
    }

    /**
     * Constructor for testing with a specific server port.
     * The {@link InnerServer} will listen on the given port (no TLS).
     */
    public CmsNode(int serverPort) {
        this.server = serverPort > 0 ? new InnerServer(serverPort, 0) : null;
    }

    public void registerServer(ServiceHandler handler) {
        if (server != null) server.register(handler);
    }

    public void registerClient(Object handler) {
        clientHandlers.put(handler.getClass(), handler);
    }

    @SuppressWarnings("unchecked")
    public <T> T getClient(Class<T> type) {
        return (T) clientHandlers.get(type);
    }

    /**
     * Execute a registered client handler with the given args.
     * Looks up the handler by class, finds its {@code execute} method,
     * and invokes it.
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

    /**
     * Start the server node.
     *
     * @param test if {@code true}, load SCL from {@code testSclFiles} (for unit/integration tests);
     *             otherwise load from {@code sclFiles} (for production server console)
     */
    public void start(boolean test) throws IOException {
        if (server != null) {
            CmsConfig.Server cfg = CmsConfigLoader.load().getServer();
            String sclFile = test ? cfg.getResolvedTestSclFile() : cfg.getResolvedSclFile();
            log.info("SCL file resolved: {} (test={})", sclFile, test);
            if (sclFile != null) {
                sclManager.load(sclFile);
                if (sclManager.isLoaded()) {
                    server.setSclDocument(sclManager.getDocument());
                }
            }
            server.start();
        }
    }

    public SclManager getSclManager() { return sclManager; }
    public ContentManager getContentManager() { return contentManager; }
    public GmCredentialManager getCredentialManager() { return credentialManager; }
    public void setCredentialManager(GmCredentialManager credentialManager) { this.credentialManager = credentialManager; }

    public void stop() {
        client.close();
        if (server != null) server.stop();
    }

    public void connect(String host, int port) throws IOException {
        client.connect(host, port);
    }

    public void connectTls(String host, int port, SSLContext sslContext) throws IOException {
        client.connectTls(host, port, sslContext);
    }

    public Frame sendRequest(ServiceName sc, byte[] asduBytes, long timeoutMs) throws IOException {
        return client.sendRequest(sc, asduBytes, timeoutMs);
    }

    public Frame sendRequest(ServiceName sc, byte[] asduBytes) throws IOException {
        return client.sendRequest(sc, asduBytes);
    }

    public void close() { client.close(); }
    public boolean isClientConnected() { return client.isConnected(); }
    public boolean isServerRunning() { return server != null && server.isRunning(); }
    public InnerServer getServer() { return server; }
    public InnerClient getClient() { return client; }
}
