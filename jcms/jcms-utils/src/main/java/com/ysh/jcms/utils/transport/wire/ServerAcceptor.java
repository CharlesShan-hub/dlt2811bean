package com.ysh.jcms.utils.transport.wire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ServerAcceptor — accepts incoming TCP/TLS connections.
 *
 * <p>Each accepted connection is wrapped in a {@link Connection} and
 * its reader thread is started automatically.
 */
public class ServerAcceptor {

    private static final Logger log = LoggerFactory.getLogger(ServerAcceptor.class);

    private final int port;
    private final ConnectionListener listener;
    private final CopyOnWriteArrayList<Connection> connections = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private volatile boolean running;
    private Thread acceptorThread;
    private javax.net.ssl.SSLContext sslContext;
    private boolean needClientAuth;

    public ServerAcceptor(int port, ConnectionListener listener) {
        this.port = port;
        this.listener = listener;
    }

    public ServerAcceptor sslContext(javax.net.ssl.SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public ServerAcceptor needClientAuth(boolean need) {
        this.needClientAuth = need;
        return this;
    }

    /** Start listening. */
    public void start() throws IOException {
        serverSocket = createServerSocket();
        running = true;
        acceptorThread = new Thread(this::acceptLoop, "cms-acceptor");
        acceptorThread.setDaemon(true);
        acceptorThread.start();
        log.info("ServerAcceptor listening on port {}", port);
    }

    private ServerSocket createServerSocket() throws IOException {
        if (sslContext != null) {
            SSLServerSocket socket = (SSLServerSocket) sslContext
                    .getServerSocketFactory().createServerSocket(port);
            if (needClientAuth) socket.setNeedClientAuth(true);
            else socket.setWantClientAuth(true);
            return socket;
        }
        return new ServerSocket(port);
    }

    private void acceptLoop() {
        while (running && !serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                Connection conn = new Connection(socket, listener);
                connections.add(conn);
                conn.startReader();
                listener.onConnected(conn);
            } catch (IOException e) {
                if (running) log.error("Accept failed", e);
            }
        }
    }

    /** Stop listening and close all connections. */
    public void stop() {
        running = false;
        try { serverSocket.close(); } catch (Exception ignored) {}
        for (Connection conn : connections) conn.close();
        connections.clear();
    }

    public boolean isRunning() { return running; }
    public int getPort() { return port; }
    public java.util.List<Connection> getConnections() {
        return new java.util.ArrayList<>(connections);
    }
}
