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
    private Thread acceptorThread;
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
     * 中断阻塞中的 accept() 调用，让 stop() 能及时返回
     */
    private void interruptAccept() {
        try {
            if (serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed()) {
                // 连接一个"取消连接"到本地端口，中断 accept()
                // 使用 127.0.0.1 而不是 getLocalSocketAddress() 以避免潜在的问题
                java.net.InetSocketAddress addr = new java.net.InetSocketAddress("127.0.0.1", port);
                try (java.net.Socket cancelSocket = new java.net.Socket()) {
                    cancelSocket.connect(addr, 50);
                }
            }
        } catch (Exception ignored) {
            // 忽略所有异常，中断成功或失败都不影响 stop() 流程
        }
    }

    /**
     * Stops the server and closes all connections.
     */
    public void stop() {
        running = false;
        // 先中断阻塞中的 accept()（在 Windows 上 close() 不会立即中断）
        interruptAccept();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
        // 等待 acceptor 线程结束，确保端口被释放
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
            Socket socket = null;
            try {
                socket = serverSocket.accept();

                // TLS 握手
                if (socket instanceof javax.net.ssl.SSLSocket) {
                    ((javax.net.ssl.SSLSocket) socket).startHandshake();
                }

                CmsConnection conn = new CmsConnection(socket, wrapListener(socket));
                connections.add(conn);
                listener.onConnected(conn);
                conn.startReadLoop();
            } catch (IOException e) {
                // 确保 socket 被关闭，防止资源泄漏
                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                }
                // socket 关闭或出错时退出循环
                if (!running || serverSocket.isClosed()) {
                    break;
                }
                listener.onError(null, e);
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
