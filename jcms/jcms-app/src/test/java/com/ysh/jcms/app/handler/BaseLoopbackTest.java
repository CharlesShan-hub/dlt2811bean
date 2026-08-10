package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

/**
 * Base class for loopback integration tests.
 *
 * <p>
 * Manages a client-server pair on a free port:
 * <ul>
 * <li>{@link #setup()} starts the server and connects the client</li>
 * <li>{@link #cleanup()} closes the client and stops the server</li>
 * </ul>
 *
 * <p>
 * Subclasses register their service handlers in the constructor:
 *
 * <pre>
 * {
 *     &#64;code
 *     public class FooLoopbackTest extends BaseLoopbackTest {
 *         public FooLoopbackTest() {
 *             registerServer(new FooServer());
 *             registerClient(new FooClient(clientNode));
 *         }
 *     }
 * }
 * </pre>
 */
public abstract class BaseLoopbackTest {

    private CmsNode serverNode;
    private CmsNode clientNode;
    private static final int FREE_PORT = findFreePort();

    @Before
    public void setup() throws Exception {
        serverNode = new CmsNode(FREE_PORT);
        registerServers(serverNode);
        serverNode.start(true);

        clientNode = new CmsNode(0);
        registerClients(clientNode);
        clientNode.connect("127.0.0.1", FREE_PORT);
    }

    @After
    public void cleanup() {
        if (clientNode != null)
            clientNode.close();
        if (serverNode != null)
            serverNode.stop();
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
    // Protected accessors
    // ──────────────────────────────────────────────

    protected CmsNode serverNode() {
        return serverNode;
    }
    protected CmsNode clientNode() {
        return clientNode;
    }

    /**
     * Convenience: register a server handler.
     */
    protected static void regServer(CmsNode node, ServiceHandler handler) {
        node.registerServer(handler);
    }

    /**
     * Convenience: register a client handler.
     */
    protected static void regClient(CmsNode node, BaseClientHandler handler) {
        handler.node(node);
        node.registerClient(handler);
    }

    /**
     * Convenience: execute a registered client handler via reflection.
     */
    protected static <T> T exec(CmsNode node, Class<?> handlerClass, Object... args) throws Exception {
        return node.execute(handlerClass, args);
    }

    /**
     * Convenience: associate with the default test sapRef "E1Q1SB1/S1".
     * <p>
     * Only call this after the {@link AssociateServer} and {@link AssociateClient}
     * have been registered (otherwise it will fail with NullPointerException).
     */
    protected void associate() throws Exception {
        clientNode().getClient(AssociateClient.class).execute(new AssociateClientDao().sapRef("E1Q1SB1/S1").secure(false));
        assertEquals(SessionState.ASSOCIATED, clientNode().client().session().state());
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
