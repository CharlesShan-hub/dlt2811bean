package com.ysh.jcms.utils.transport.wire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ServerAcceptor — accepts incoming TCP/TLS connections.
 *
 * <p>
 * Each accepted connection is wrapped in a {@link Connection} and its reader
 * thread is started automatically.
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class ServerAcceptor {

    private static final Logger log = LoggerFactory.getLogger(ServerAcceptor.class);

    private final int port;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final ConnectionListener listener;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final CopyOnWriteArrayList<Connection> connections = new CopyOnWriteArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private ServerSocket serverSocket;
    @Setter(AccessLevel.NONE)
    private volatile boolean running;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Thread acceptorThread;
    @Getter(AccessLevel.NONE)
    private javax.net.ssl.SSLContext sslContext;
    @Getter(AccessLevel.NONE)
    private boolean needClientAuth;

    public ServerAcceptor(int port, ConnectionListener listener) {
        this.port = port;
        this.listener = listener;
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
            // DL/T 2811 B.2: transport security via TLS; service port 9102 (6.6)
            SSLServerSocket socket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port);
            if (needClientAuth)
                socket.setNeedClientAuth(true);
            else
                socket.setWantClientAuth(true);
            return socket;
        }
        return new ServerSocket(port); // DL/T 2811 6.6: plain service port 8102
    }

    private void acceptLoop() {
        while (running && !serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                // DL/T 2811 6.7: one connection per client IP — close the old one on duplicate
                InetAddress clientAddr = socket.getInetAddress();
                for (Connection old : connections) {
                    if (clientAddr.equals(old.socket().getInetAddress())) {
                        old.close(); // reader thread drops it from the registry via onClosed
                    }
                }
                Connection conn = new Connection(socket, listener);
                conn.onClosed(() -> connections.remove(conn)); // drop dead connections from the registry
                connections.add(conn);
                conn.startReader();
                listener.onConnected(conn);
            } catch (IOException e) {
                if (running)
                    log.error("Accept failed", e);
            }
        }
    }

    /** Stop listening and close all connections. */
    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (Exception ignored) {
        }
        for (Connection conn : connections)
            conn.close();
        connections.clear();
    }

    public java.util.List<Connection> connections() {
        return new java.util.ArrayList<>(connections);
    }
}
