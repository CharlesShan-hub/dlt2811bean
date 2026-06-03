package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.config.CmsConfigInjector;
import com.ysh.dlt2811bean.config.CmsValue;
import com.ysh.dlt2811bean.security.GmSslContext;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;

/**
 * Client-side transport factory.
 *
 * <p>Features:
 * <ol>
 *   <li>Plain TCP and GM (Guomi) TLS connections
 *   <li>Simple factory API — creates connected {@link CmsConnection} instances
 *   <li>Fluent configuration via {@link #sslContext(GmSslContext)}
 * </ol>
 */
public class CmsClientTransport {

    @CmsValue("client.connectTimeoutMs")
    private int connectTimeout = 5000;

    private GmSslContext sslContext;

    public CmsClientTransport() {
        CmsConfigInjector.inject(this);
    }

    /* ==================== Configuration ==================== */

    public CmsClientTransport sslContext(GmSslContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    /* ==================== Connect ==================== */

    public CmsConnection connect(String host, int port, CmsTransportListener listener) throws IOException {
        Socket socket = createSocket();
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        return new CmsConnection(socket, listener);
    }

    public CmsConnection connectTls(String host, int port, CmsTransportListener listener) throws IOException {
        if (sslContext == null) {
            throw new IllegalStateException("SSL context not set, call sslContext() first");
        }

        try {
            SSLSocket socket = (SSLSocket) sslContext.getSslContext()
                    .getSocketFactory()
                    .createSocket(host, port);

            socket.setEnabledProtocols(sslContext.getEnabledProtocols());

            try {
                socket.setEnabledCipherSuites(sslContext.getEnabledCipherSuites());
            } catch (Exception ignored) {
            }

            socket.setUseClientMode(true);
            socket.startHandshake();

            return new CmsConnection(socket, listener);
        } catch (Exception e) {
            throw new IOException("TLS connection failed: " + e.getMessage(), e);
        }
    }

    /* ==================== Internal ==================== */

    private Socket createSocket() throws IOException {
        return new Socket();
    }

    /* ==================== Status ==================== */

    public boolean isTlsEnabled() {
        return sslContext != null;
    }
}
