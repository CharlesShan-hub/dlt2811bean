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
 * <p>Supports both plain TCP and 国密 TLS connections.
 * Each accepted connection is wrapped in a {@link CmsConnection} and notified
 * via {@link CmsTransportListener#onConnected}.
 */
public class CmsServerTransport {

    private final int port;
    private final CmsTransportListener listener;
    private final CopyOnWriteArrayList<CmsConnection> connections = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private volatile boolean running;
    private GmSslContext sslContext;
    private boolean needClientAuth = false;  // 暂存配置

    /**
     * Creates a server transport with plain TCP.
     *
     * @param port     the port to listen on
     * @param listener event listener for all connections
     */
    public CmsServerTransport(int port, CmsTransportListener listener) {
        this.port = port;
        this.listener = listener;
    }

    /**
     * Sets the 国密 SSL context for TLS connections.
     * If not set, plain TCP is used.
     *
     * @param sslContext the SSL context
     * @return this transport for chaining
     */
    public CmsServerTransport sslContext(GmSslContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    /**
     * Sets whether client certificate is required (mutual TLS).
     * Must be called before {@link #start()}.
     *
     * @param need true to require client certificate
     * @return this transport for chaining
     */
    public CmsServerTransport needClientAuth(boolean need) {
        this.needClientAuth = need;
        return this;
    }

    /**
     * Starts listening for connections.
     *
     * @throws IOException if the port cannot be bound
     */
    public void start() throws IOException {
        serverSocket = createServerSocket();
        running = true;
        Thread acceptor = new Thread(this::acceptLoop, "cms-acceptor");
        acceptor.setDaemon(true);
        acceptor.start();
    }

    private ServerSocket createServerSocket() throws IOException {
        if (sslContext != null) {
            try {
                SSLServerSocket socket = (SSLServerSocket) sslContext.getSslContext()
                        .getServerSocketFactory()
                        .createServerSocket(port);

                // 设置国密 TLS 协议版本和加密套件
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

    /**
     * Stops the server and closes all connections.
     */
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
        for (CmsConnection conn : connections) {
            conn.close();
        }
        connections.clear();
    }

    /**
     * @return true if the server is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return the port this server is listening on
     */
    public int getPort() {
        return port;
    }

    /**
     * @return true if the server socket is bound and listening
     */
    public boolean isBound() {
        return serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed();
    }

    /**
     * @return true if TLS is enabled
     */
    public boolean isTlsEnabled() {
        return sslContext != null;
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();

                // TLS 握手
                if (socket instanceof javax.net.ssl.SSLSocket) {
                    ((javax.net.ssl.SSLSocket) socket).startHandshake();
                }

                CmsConnection conn = new CmsConnection(socket, wrapListener(socket));
                connections.add(conn);
                listener.onConnected(conn);
                conn.startReadLoop();
            } catch (IOException e) {
                if (running) {
                    listener.onError(null, e);
                }
            }
        }
    }

    private CmsTransportListener wrapListener(Socket socket) {
        return new CmsTransportListener() {
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
}
