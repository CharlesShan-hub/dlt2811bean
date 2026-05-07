package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-side transport that listens on a port and accepts incoming connections.
 *
 * <p>Features:
 * <ol>
 *   <li>Plain TCP and GM (Guomi) TLS connections
 *   <li>Each connection wrapped in a {@link CmsConnection} with lifecycle callbacks
 *   <li>Automatic connection tracking and cleanup on stop
 * </ol>
 */
public class CmsServerTransport {

    private final int port;
    private final CmsTransportListener listener;
    private final CopyOnWriteArrayList<CmsConnection> connections = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private volatile boolean running;
    private Thread acceptorThread;
    private GmSslContext sslContext;
    private boolean needClientAuth = false;

    /* ==================== Construction ==================== */

    public CmsServerTransport(int port, CmsTransportListener listener) {
        this.port = port;
        this.listener = listener;
    }

    /* ==================== Configuration ==================== */

    public CmsServerTransport sslContext(GmSslContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public CmsServerTransport needClientAuth(boolean need) {
        this.needClientAuth = need;
        return this;
    }

    /* ==================== Lifecycle ==================== */

    public void start() throws IOException {
        serverSocket = createServerSocket();
        running = true;
        acceptorThread = new Thread(this::acceptLoop, "cms-acceptor");
        acceptorThread.setDaemon(true);
        acceptorThread.start();
    }

    private ServerSocket createServerSocket() throws IOException {
        if (sslContext != null) {
            try {
                SSLServerSocket socket = (SSLServerSocket) sslContext.getSslContext()
                        .getServerSocketFactory()
                        .createServerSocket(port);

                socket.setEnabledProtocols(sslContext.getEnabledProtocols());
                socket.setEnabledCipherSuites(sslContext.getEnabledCipherSuites());

                if (needClientAuth) {
                    socket.setNeedClientAuth(true);
                } else {
                    socket.setWantClientAuth(true);
                }
                return socket;
            } catch (Exception e) {
                throw new IOException("Failed to create SSL server socket: " + e.getMessage(), e);
            }
        }
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        return serverSocket;
    }

    public void stop() {
        running = false;
        interruptAccept();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
        if (acceptorThread != null) {
            try {
                acceptorThread.join(1000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            acceptorThread = null;
        }
        for (CmsConnection conn : connections) {
            conn.close();
        }
        connections.clear();
    }

    private void interruptAccept() {
        try {
            if (serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed()) {
                new java.net.Socket("127.0.0.1", port).close();
            }
        } catch (Exception ignored) {
        }
    }

    /* ==================== Status ==================== */

    public boolean isRunning() {
        return running;
    }

    public int getPort() {
        return port;
    }

    public boolean isBound() {
        return serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed();
    }

    public boolean isTlsEnabled() {
        return sslContext != null;
    }

    /* ==================== Internal ==================== */

    private void acceptLoop() {
        while (running) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();

                if (socket instanceof javax.net.ssl.SSLSocket) {
                    ((javax.net.ssl.SSLSocket) socket).startHandshake();
                }

                CmsConnection conn = new CmsConnection(socket, connectionListener);
                connections.add(conn);
                listener.onConnected(conn);
                conn.startReadLoop();
            } catch (IOException e) {
                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                }
                if (!running || serverSocket.isClosed()) {
                    break;
                }
                listener.onError(null, e);
            }
        }
    }

    private final CmsTransportListener connectionListener = new CmsTransportListener() {
        @Override
        public void onConnected(CmsConnection connection) {
            listener.onConnected(connection);
        }

        @Override
        public void onApduReceived(CmsConnection connection, CmsApdu apdu) {
            listener.onApduReceived(connection, apdu);
        }

        @Override
        public void onDisconnected(CmsConnection connection) {
            connections.remove(connection);
            listener.onDisconnected(connection);
        }

        @Override
        public void onError(CmsConnection connection, Exception e) {
            listener.onError(connection, e);
        }
    };
}
