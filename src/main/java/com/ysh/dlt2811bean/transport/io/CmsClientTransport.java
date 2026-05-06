package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.security.GmSslContext;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;

/**
 * Client-side transport factory.
 *
 * <p>Creates {@link CmsConnection} instances by connecting to a server.
 * Supports both plain TCP and TLS (国密 SSL) connections.
 */
public class CmsClientTransport {

    private GmSslContext sslContext;

    /**
     * Sets the 国密 SSL context for secure connections.
     * If not set, plain TCP connection is used.
     *
     * @param sslContext the SSL context
     * @return this transport for chaining
     */
    public CmsClientTransport sslContext(GmSslContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    /**
     * Connects to a CMS server using plain TCP.
     *
     * @param host     server hostname or IP address
     * @param port     server port
     * @param listener event listener for the new connection
     * @return a connected CmsConnection
     * @throws IOException if the connection fails
     */
    public CmsConnection connect(String host, int port, CmsTransportListener listener) throws IOException {
        Socket socket = createSocket();
        socket.connect(new java.net.InetSocketAddress(host, port), 5000);
        return new CmsConnection(socket, listener);
    }

    /**
     * Connects to a CMS server using 国密 TLS.
     * Requires {@link #sslContext(GmSslContext)} to be called first.
     *
     * @param host     server hostname or IP address
     * @param port     server port
     * @param listener event listener for the new connection
     * @return a connected CmsConnection
     * @throws IOException if the connection fails
     */
    public CmsConnection connectTls(String host, int port, CmsTransportListener listener) throws IOException {
        if (sslContext == null) {
            throw new IllegalStateException("SSL context not set, call sslContext() first");
        }

        try {
            SSLSocket socket = (SSLSocket) sslContext.getSslContext()
                    .getSocketFactory()
                    .createSocket(host, port);

            // 设置国密 TLS 协议版本
            socket.setEnabledProtocols(sslContext.getEnabledProtocols());

            // 尝试设置加密套件，如果不支持则忽略
            try {
                socket.setEnabledCipherSuites(sslContext.getEnabledCipherSuites());
            } catch (Exception e) {
                // 忽略加密套件设置错误，使用默认值
            }

            // 强制 TLS 握手
            socket.setUseClientMode(true);
            socket.startHandshake();

            return new CmsConnection(socket, listener);
        } catch (Exception e) {
            throw new IOException("TLS connection failed: " + e.getMessage(), e);
        }
    }

    private Socket createSocket() throws IOException {
        if (sslContext != null) {
            try {
                SSLSocket socket = (SSLSocket) sslContext.getSslContext()
                        .getSocketFactory()
                        .createSocket();
                socket.setUseClientMode(true);
                return socket;
            } catch (Exception e) {
                throw new IOException("Failed to create SSL socket: " + e.getMessage(), e);
            }
        }
        return new Socket();
    }

    /**
     * @return true if TLS is configured
     */
    public boolean isTlsEnabled() {
        return sslContext != null;
    }
}
