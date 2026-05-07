package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.security.GmAuthenticator;
import com.ysh.dlt2811bean.security.GmCertificateParser;
import com.ysh.dlt2811bean.security.GmSignature;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.security.GmTrustManager;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.io.CmsClientTransport;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import com.ysh.dlt2811bean.transport.io.CmsTransportListener;
import com.ysh.dlt2811bean.transport.session.CmsClientSession;
import com.ysh.dlt2811bean.transport.session.PendingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 * CMS Client — Application layer entry point.
 *
 * <p>Provides high-level operations: connect, associate, release, abort, test.
 * Handles ReqID assignment and response matching automatically.
 *
 * <p>Example:
 * <pre>
 * CmsClient client = new CmsClient();
 * client.connect("127.0.0.1", 8888);
 * client.associate();     // 建立关联
 * client.test();          // 心跳测试
 * client.release();        // 释放关联
 * client.close();
 * </pre>
 *
 * <p>With GM security:
 * <pre>
 * CmsClient client = new CmsClient();
 * client.enableSecurity();  // 启用国密认证
 * client.connect("127.0.0.1", 8888);
 * client.associate();       // 自动携带证书
 * </pre>
 */
public class CmsClient {

    private static final Logger log = LoggerFactory.getLogger(CmsClient.class);

    private final CmsClientTransport transport = new CmsClientTransport();
    private final ClientListener listener = new ClientListener();
    private volatile CmsConnection connection;
    private volatile CmsClientSession session;

    private String defaultAp = "E1Q1SB1";
    private String defaultEp = "S1";

    // ==================== Security (GM) ====================

    private boolean securityEnabled = false;
    private KeyPair securityKeyPair;
    private GmAuthenticator securityAuthenticator;
    private java.security.cert.X509Certificate securityCertificate;
    private java.security.cert.X509Certificate serverCertificate;

    // ==================== Connection ====================

    public void connect(String host, int port) throws Exception {
        connection = transport.connect(host, port, listener);
        session = new CmsClientSession(connection);
        connection.startReadLoop();
        log.info("Connected to {}:{}", host, port);
    }

    public void connectTls(String host, int port) throws Exception {
        if (!transport.isTlsEnabled()) {
            throw new IllegalStateException("SSL context not set, call sslContext() first");
        }
        connection = transport.connectTls(host, port, listener);
        session = new CmsClientSession(connection);
        connection.startReadLoop();
        log.info("Connected to {}:{} (TLS)", host, port);
    }

    public void close() {
        if (connection != null) {
            connection.close();
            connection = null;
            session = null;
            log.info("Connection closed");
        }
    }

    // ==================== Config ====================

    public CmsClient sslContext(GmSslContext sslContext) {
        transport.sslContext(sslContext);
        return this;
    }

    public boolean isTlsEnabled() {
        return transport.isTlsEnabled();
    }

    public CmsClient setAccessPoint(String ap, String ep) {
        this.defaultAp = ap;
        this.defaultEp = ep;
        return this;
    }

    /**
     * Enables GM (Guomi) security for this client.
     * When enabled, associate() will automatically include authentication certificate.
     *
     * <p>This method:
     * <ul>
     *   <li>Generates a SM2 key pair</li>
     *   <li>Generates a self-signed SM2 certificate</li>
     *   <li>Creates a trust manager that trusts all certificates</li>
     *   <li>Creates an authenticator for certificate verification</li>
     * </ul>
     *
     * @return this client for chaining
     * @throws Exception if key pair or certificate generation fails
     */
    public CmsClient enableSecurity() throws Exception {
        // Generate SM2 key pair
        this.securityKeyPair = GmSignature.generateKeyPair();

        // Generate self-signed SM2 certificate
        this.securityCertificate = GmSignature.generateSelfSignedCertificate(securityKeyPair);

        // Create trust manager that trusts all (for receiving certificates)
        GmTrustManager trustManager = new GmTrustManager();

        // Create authenticator
        this.securityAuthenticator = new GmAuthenticator(trustManager);

        this.securityEnabled = true;
        log.info("GM security enabled, key pair and certificate generated");
        return this;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    // ==================== Internal Echo ====================

    private CmsApdu testEcho(CmsTest asdu) throws Exception {
        PendingRequest pending = session.addPendingRequest(0);  // ReqID=0 for Test
        CmsApdu apdu = new CmsApdu(asdu);
        session.send(apdu);
        log.debug("Sent TEST, waiting for echo...");

        CmsApdu response = (CmsApdu) pending.await(session.getDefaultTimeoutMs());
        if (response == null) {
            session.removePendingRequest(0);
        }
        return response;
    }

    // ==================== Internal Send ====================

    private CmsApdu send(CmsAsdu<?> asdu) throws Exception {
        int reqId = session.nextReqId();
        asdu.reqId(reqId);
        PendingRequest pending = session.addPendingRequest(reqId);

        CmsApdu request = new CmsApdu(asdu);
        session.send(request);
        log.debug("[ReqID={}] Sent {}", reqId, asdu.getClass().getSimpleName());

        CmsApdu response = (CmsApdu) pending.await(session.getDefaultTimeoutMs());
        if (response == null) {
            session.removePendingRequest(reqId);
            throw new java.util.concurrent.TimeoutException("Request timeout");
        }
        return response;
    }

    private void doSendWithoutResponse(CmsAsdu<?> asdu) throws Exception {
        int reqId = session.nextReqId();
        asdu.reqId(reqId);
        CmsApdu request = new CmsApdu(asdu);
        session.send(request);
        log.debug("[ReqID={}] Sent {} (one-way)", reqId, asdu.getClass().getSimpleName());
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    public byte[] getAssociationId() {
        return session != null ? session.getAssociationId() : null;
    }

    public void setAssociationId(byte[] id) {
        if (session != null) {
            session.setAssociationId(id);
        }
    }

    // ==================== Transport Listener ====================

    private class ClientListener implements CmsTransportListener {

        @Override
        public void onConnected(CmsConnection connection) {
            log.debug("Connected");
        }

        @Override
        public void onApduReceived(CmsConnection conn, CmsApdu apdu) {
            if (session == null) {
                return;
            }

            // Test echo: complete the pending request registered with ReqID=0
            if (apdu.getReqId() == 0 && apdu.getApch().getServiceCode() == ServiceName.TEST) {
                PendingRequest pending = session.removePendingRequest(0);
                if (pending != null) {
                    pending.setResult(apdu);
                    log.debug("Received Test echo, response delivered");
                    return;
                }
            }

            session.dispatchResponse(apdu);
        }

        @Override
        public void onDisconnected(CmsConnection conn) {
            if (session != null) {
                session.onDisconnected();
            }
        }

        @Override
        public void onError(CmsConnection conn, Exception e) {
            log.error("Connection error: {}", e.getMessage(), e);
        }
    }

    // ==================== Security Helper ====================

    /**
     * Creates an authentication parameter with SM2 signature.
     * 
     * <p>The signed data is: serverAccessPointReference + timestamp
     * This must match the server's prepareSignedData() method.
     */
    private AuthenticationParameter createAuthenticationParameter() throws Exception {
        // Use the self-signed certificate
        byte[] certBytes = securityCertificate.getEncoded();

        // Create timestamp
        long now = System.currentTimeMillis();
        long seconds = now / 1000;
        // Use microseconds as fractionOfSecond (fits in INT24U: max 999999 < 16777215)
        int fractionOfSecond = (int) ((now % 1000) * 1000); // milliseconds to microseconds

        // Create signed data: serverAccessPointReference (full format: AP.EP) + timestamp
        // This must match AssociateHandler.prepareSignedData()
        String fullSap = defaultAp + "." + defaultEp;
        byte[] sapBytes = fullSap.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] timeBytes = String.valueOf(seconds).getBytes();
        
        byte[] signedData = new byte[sapBytes.length + timeBytes.length];
        System.arraycopy(sapBytes, 0, signedData, 0, sapBytes.length);
        System.arraycopy(timeBytes, 0, signedData, sapBytes.length, timeBytes.length);
        
        byte[] signature = GmSignature.sign(securityKeyPair.getPrivate(), signedData);

        // Build authentication parameter
        AuthenticationParameter authParam = new AuthenticationParameter()
                .signatureCertificate(certBytes)
                .signedTime(seconds, fractionOfSecond, 24)
                .signedValue(signature);

        log.debug("Created authentication parameter, cert length={}, signature length={}, sap={}",
                certBytes.length, signature.length, fullSap);

        return authParam;
    }

    // ========================== Public Services API ========================

    /**
     * Associate - associate - Service Code 01
     * Sends an Associate request to establish a connection.
     * If security is enabled, automatically includes authentication certificate.
     *
     * @param ap        the access point name (optional, uses default if null)
     * @param ep        the end point name (optional, uses default if null)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu associate() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference(defaultAp, defaultEp);

        if (securityEnabled && securityKeyPair != null) {
            AuthenticationParameter authParam = createAuthenticationParameter();
            asdu.authenticationParameter(authParam);
        }

        CmsApdu response = send(asdu);

        // Save server certificate from response if present
        if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            CmsAssociate responseAsdu = (CmsAssociate) response.getAsdu();
            if (responseAsdu.authenticationParameter() != null &&
                responseAsdu.authenticationParameter().signatureCertificate() != null) {
                byte[] certBytes = responseAsdu.authenticationParameter().signatureCertificate().get();
                this.serverCertificate = GmCertificateParser.parseX509(certBytes);
            }
        }

        return response;
    }

    public CmsApdu associate(String ap, String ep) throws Exception {
        setAccessPoint(ap, ep);
        return associate();
    }


    /**
     * Associate - release - Service Code 02
     * Sends a Release request to terminate the connection.
     *
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu release() throws Exception {
        CmsRelease asdu = new CmsRelease(MessageType.REQUEST);
        CmsApdu response = send(asdu);
        if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            setAssociationId(null);
        }
        return response;
    }

    /**
     * Associate - abort - Service Code 03
     * Sends an Abort request to terminate the connection.
     *
     * @param reason the reason for aborting (optional)
     * @throws Exception if not connected or timeout
     */
    public void abort() throws Exception {
        abort(0);
    }

    public void abort(int reason) throws Exception {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST).reason(reason);
        doSendWithoutResponse(asdu);
        close();
    }

    /**
     * Directory - getServerDirectory - Service Code 80
     * Sends a GetServerDirectory request to retrieve the server directory.
     *
     * @param referenceAfter the reference after which to continue the search (optional)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */

    public CmsApdu getServerDirectory() throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
                .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
        return send(asdu);
    }

    public CmsApdu getServerDirectory(String referenceAfter) throws Exception {
        CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
                .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE))
                .referenceAfter(referenceAfter);
        return send(asdu);
    }

    /**
     * Directory - getLogicalDeviceDirectory - Service Code 81
     * Sends a GetLogicalDeviceDirectory request to retrieve the logical device directory.
     * 
     * @param ldName the logical device name (optional)
     * @param referenceAfter the reference after which to continue the search (optional)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */

    public CmsApdu getLogicalDeviceDirectory() throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
        return send(asdu);
    }

    public CmsApdu getLogicalDeviceDirectory(String ldName) throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST)
                .ldName(ldName);
        return send(asdu);
    }

    public CmsApdu getLogicalDeviceDirectory(String ldName, String referenceAfter) throws Exception {
        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
        if (ldName != null) {
            asdu.ldName(ldName);
        }
        if (referenceAfter != null) {
            asdu.referenceAfter(referenceAfter);
        }
        return send(asdu);
    }

    /**
     * Directory - getLogicalNodeDirectoryByLd - Service Code 82
     * Sends a GetLogicalNodeDirectoryByLd request to retrieve the logical node directory by logical device name.
     * 
     * @param ldName the logical device name (optional)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getLogicalNodeDirectoryByLd(String ldName) throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .ldName(ldName);
        return send(asdu);
    }

    public CmsApdu getLogicalNodeDirectoryByLd(String ldName, String referenceAfter) throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .ldName(ldName);
        if (referenceAfter != null) {
            asdu.referenceAfter(referenceAfter);
        }
        return send(asdu);
    }

    public CmsApdu getLogicalNodeDirectoryByLn(String lnReference) throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .lnReference(lnReference);
        return send(asdu);
    }

    public CmsApdu getLogicalNodeDirectoryByLn(String lnReference, String referenceAfter) throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .lnReference(lnReference);
        if (referenceAfter != null) {
            asdu.referenceAfter(referenceAfter);
        }
        return send(asdu);
    }

    /**
     * Test - test - Service Code 153
     * Sends a Test request to verify the connection.
     *
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu test() throws Exception {
        CmsTest asdu = new CmsTest(MessageType.REQUEST);
        return testEcho(asdu);
    }
}
