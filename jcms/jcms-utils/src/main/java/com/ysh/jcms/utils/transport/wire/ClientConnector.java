package com.ysh.jcms.utils.transport.wire;

import java.io.IOException;
import java.net.Socket;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Client-side connector — establishes outbound TCP/TLS connections.
 */
@Setter
@Accessors(fluent = true, chain = true)
public class ClientConnector {

    private int connectTimeout = 5000;

    /**
     * Establish a plain TCP connection.
     */
    public Connection connect(String host, int port, ConnectionListener listener) throws IOException {
        Socket socket = new Socket();
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        return new Connection(socket, listener);
    }

    /**
     * Establish a TLS connection (DL/T 2811 B.2: transport-layer security via TLS).
     */
    public Connection connectTls(String host, int port, ConnectionListener listener, javax.net.ssl.SSLContext sslContext)
            throws IOException {
        javax.net.ssl.SSLSocket socket = null;
        try {
            socket = (javax.net.ssl.SSLSocket) sslContext.getSocketFactory().createSocket(host, port);
            socket.setUseClientMode(true);
            // Handshake timeout: fail fast when hitting a non-TLS port, avoid blocking
            // forever
            socket.setSoTimeout(connectTimeout);
            socket.startHandshake();
            socket.setSoTimeout(0); // resume blocking reads after the handshake
            return new Connection(socket, listener);
        } catch (Exception e) {
            // Close the socket on failure so a bad handshake cannot leak an fd
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
            throw new IOException("TLS connection failed: " + e.getMessage(), e);
        }
    }
}
