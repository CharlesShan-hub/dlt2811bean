package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Base class for loopback integration tests.
 *
 * <p>Manages a client-server pair on a free port:
 * <ul>
 *   <li>{@link #setup()} starts the server and connects the client</li>
 *   <li>{@link #cleanup()} closes the client and stops the server</li>
 * </ul>
 *
 * <p>Subclasses register their service handlers in the constructor:
 * <pre>{@code
 * public class FooLoopbackTest extends BaseLoopbackTest {
 *     public FooLoopbackTest() {
 *         registerServer(new FooServer());
 *         registerClient(new FooClient(clientNode));
 *     }
 * }
 * }</pre>
 */
public abstract class BaseLoopbackTest {

    private static final int FREE_PORT = findFreePort();
    private CmsNode serverNode;
    private CmsNode clientNode;

    @Before
    public void setup() throws Exception {
        serverNode = new CmsNode(FREE_PORT);
        registerServers(serverNode);
        serverNode.start();

        clientNode = new CmsNode(0);
        registerClients(clientNode);
        clientNode.connect("127.0.0.1", FREE_PORT);
    }

    @After
    public void cleanup() {
        if (clientNode != null) clientNode.close();
        if (serverNode != null) serverNode.stop();
    }

    /**
     * Register server-side handlers. Called during {@link #setup()}.
     */
    protected abstract void registerServers(CmsNode node) throws Exception;

    /**
     * Register client-side handlers. Called during {@link #setup()}.
     */
    protected abstract void registerClients(CmsNode node) throws Exception;

    // ──────────────────────────────────────────────
    //  Protected accessors
    // ──────────────────────────────────────────────

    protected CmsNode serverNode() { return serverNode; }
    protected CmsNode clientNode() { return clientNode; }

    /**
     * Convenience: register a server handler.
     */
    protected static void regServer(CmsNode node, ServiceHandler handler) {
        node.registerServer(handler);
    }

    /**
     * Convenience: register a client handler.
     */
    protected static void regClient(CmsNode node, Object handler) {
        node.registerClient(handler);
    }

    /**
     * Convenience: execute a registered client handler via reflection.
     */
    protected static <T> T exec(CmsNode node, Class<?> handlerClass, Object... args) throws Exception {
        return node.execute(handlerClass, args);
    }

    /**
     * Find a free TCP port for the loopback server.
     */
    private static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            return 18780;
        }
    }
}
