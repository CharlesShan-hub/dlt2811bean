package com.ysh.jcms.utils.transport.wire;

import java.io.IOException;
import java.net.Socket;

/**
 * Client-side connector — establishes outbound TCP/TLS connections.
 */
public class ClientConnector {

    private int connectTimeout = 5000;

    public ClientConnector connectTimeout(int ms) {
        this.connectTimeout = ms;
        return this;
    }

    /**
     * Establish a plain TCP connection.
     */
    public Connection connect(String host, int port, ConnectionListener listener) throws IOException {
        Socket socket = new Socket();
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        return new Connection(socket, listener);
    }

    /**
     * Establish a TLS connection.
     */
    public Connection connectTls(String host, int port, ConnectionListener listener, javax.net.ssl.SSLContext sslContext)
            throws IOException {
        try {
            javax.net.ssl.SSLSocket socket = (javax.net.ssl.SSLSocket) sslContext.getSocketFactory().createSocket(host, port);
            socket.setUseClientMode(true);
            socket.startHandshake();
            return new Connection(socket, listener);
        } catch (Exception e) {
            throw new IOException("TLS connection failed: " + e.getMessage(), e);
        }
    }
}
