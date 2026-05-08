package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigInjector;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.config.CmsValue;
import com.ysh.dlt2811bean.security.GmAuthenticator;
import com.ysh.dlt2811bean.security.GmCertificateParser;
import com.ysh.dlt2811bean.security.GmSignature;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.security.GmTrustManager;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.*;
import com.ysh.dlt2811bean.service.svc.directory.*;
import com.ysh.dlt2811bean.service.svc.data.*;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.service.svc.goose.*;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.setting.*;
import com.ysh.dlt2811bean.service.svc.report.*;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.*;
import com.ysh.dlt2811bean.service.svc.association.datatypes.*;
import com.ysh.dlt2811bean.service.svc.negotiation.*;
import com.ysh.dlt2811bean.service.svc.rpc.*;
import com.ysh.dlt2811bean.service.svc.file.*;
import com.ysh.dlt2811bean.service.svc.control.*;
import com.ysh.dlt2811bean.service.svc.sv.*;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.service.svc.dataset.*;
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

    @CmsValue("client.defaultAccessPoint")
    private String defaultAp = "E1Q1SB1";

    @CmsValue("client.defaultEp")
    private String defaultEp = "S1";

    public CmsClient() {
        CmsConfigInjector.inject(this);
    }

    // ==================== Security (GM) ====================

    private boolean securityEnabled = false;
    private KeyPair securityKeyPair;
    private GmAuthenticator securityAuthenticator;
    private X509Certificate securityCertificate;
    private X509Certificate serverCertificate;

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
        asdu.reqId(0);
        CmsApdu request = new CmsApdu(asdu);
        session.send(request);
        log.debug("[ReqID=0] Sent {} (one-way)", asdu.getClass().getSimpleName());
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

            session.onDataActivity();

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
        if (session != null && !session.isNegotiated()) {
            CmsConfig config = CmsConfigLoader.load();
            CmsApdu negResponse = associateNegotiate(
                    config.getNegotiate().getApduSize(),
                    config.getNegotiate().getAsduSize(),
                    config.getNegotiate().getProtocolVersion());
            if (negResponse == null || negResponse.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                return negResponse;
            }
        }

        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference(defaultAp, defaultEp);

        if (securityEnabled && securityKeyPair != null) {
            AuthenticationParameter authParam = createAuthenticationParameter();
            asdu.authenticationParameter(authParam);
        }

        CmsApdu response = send(asdu);

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

    public CmsApdu getLogicalNodeDirectoryByLd(String ldName, int acsiClass) throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .ldName(ldName)
                .acsiClass(new CmsACSIClass(acsiClass));
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

    public CmsApdu getLogicalNodeDirectoryByLn(String lnReference, int acsiClass) throws Exception {
        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
                .lnReference(lnReference)
                .acsiClass(new CmsACSIClass(acsiClass));
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
     * Directory - getAllDataValues - Service Code 83
     * Sends a GetAllDataValues request to retrieve all data values.
     * 
     * @param ldName the logical device name (optional)
     * @param lnReference the logical reference (optional)
     * @param fc the function class (optional)
     * @param referenceAfter the reference after which to continue the search (optional)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getAllDataValuesByLd(String ldName) throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
                .ldName(ldName);
        return send(asdu);
    }

    public CmsApdu getAllDataValuesByLd(String ldName, String fc) throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
                .ldName(ldName)
                .fc(fc);
        return send(asdu);
    }

    public CmsApdu getAllDataValuesByLn(String lnReference) throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
                .lnReference(lnReference);
        return send(asdu);
    }

    public CmsApdu getAllDataValuesByLn(String lnReference, String fc) throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.REQUEST)
                .lnReference(lnReference)
                .fc(fc);
        return send(asdu);
    }

    /**
     * Directory - getAllDataDefinitionByLd - Service Code 84
     * Sends a GetAllDataDefinitionByLd request to retrieve all data definitions by logical device name.
     * 
     * @param ldName the logical device name (optional)
     * @param lnReference the logical reference (optional)
     * @param fc the function class (optional)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getAllDataDefinitionByLd(String ldName) throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.REQUEST)
                .ldName(ldName);
        return send(asdu);
    }

    public CmsApdu getAllDataDefinitionByLd(String ldName, String fc) throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.REQUEST)
                .ldName(ldName)
                .fc(fc);
        return send(asdu);
    }

    public CmsApdu getAllDataDefinitionByLn(String lnReference) throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.REQUEST)
                .lnReference(lnReference);
        return send(asdu);
    }

    public CmsApdu getAllDataDefinitionByLn(String lnReference, String fc) throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.REQUEST)
                .lnReference(lnReference)
                .fc(fc);
        return send(asdu);
    }

    /**
     * Directory - getAllCBValues - Service Code 85
     * Sends a GetAllCBValues request to retrieve all CB values.
     * 
     * @param ldName the logical device name (optional)
     * @param lnReference the logical reference (optional)
     * @param acsiClass the ACI class (optional)
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getAllCBValuesByLd(String ldName, int acsiClass) throws Exception {
        CmsGetAllCBValues asdu = new CmsGetAllCBValues(MessageType.REQUEST)
                .ldName(ldName);
        asdu.acsiClass = new CmsACSIClass(acsiClass);
        return send(asdu);
    }

    public CmsApdu getAllCBValuesByLn(String lnReference, int acsiClass) throws Exception {
        CmsGetAllCBValues asdu = new CmsGetAllCBValues(MessageType.REQUEST)
                .lnReference(lnReference);
        asdu.acsiClass = new CmsACSIClass(acsiClass);
        return send(asdu);
    }

    /**
     * Data - getDataValues - Service Code 86
     * Sends a GetDataValues request to retrieve data values.
     * 
     * @param references the references of the data values to retrieve
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getDataValues(String... references) throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.data.add(new CmsGetDataValuesEntry().reference(ref));
        }
        return send(asdu);
    }

    public CmsApdu getDataValuesWithFc(String fc, String... references) throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.data.add(new CmsGetDataValuesEntry().reference(ref).fc(fc));
        }
        return send(asdu);
    }

    public CmsApdu setDataValues(CmsSetDataValues asdu) throws Exception {
        return send(asdu);
    }

    /**
     * DATA - getDataDirectory - Service Code 0x32
     */
    public CmsApdu getDataDirectory(String dataReference) throws Exception {
        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST)
                .dataReference(dataReference);
        return send(asdu);
    }

    /**
     * DATA - getDataDefinition - Service Code 0x33
     */
    public CmsApdu getDataDefinition(String reference) throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
                .addData(reference);
        return send(asdu);
    }

    public CmsApdu getDataDefinition(String reference, String fc) throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST)
                .addData(reference, fc);
        return send(asdu);
    }

    /**
     * Association - associateNegotiate - Service Code 9A
     * Sends an AssociateNegotiate request to negotiate service parameters before association.
     * Must be called after connect() and before associate().
     *
     * @param apduSize       the maximum APDU frame size supported by this client
     * @param asduSize       the maximum ASDU size supported by this client
     * @param protocolVersion the protocol version used by this client
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu associateNegotiate(int apduSize, int asduSize, long protocolVersion) throws Exception {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.REQUEST)
                .apduSize(apduSize)
                .asduSize(asduSize)
                .protocolVersion(protocolVersion);

        CmsApdu response = send(asdu);

        if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            CmsAssociateNegotiate respAsdu = (CmsAssociateNegotiate) response.getAsdu();
            session.setNegotiated(true);
            session.setNegotiatedApduSize(respAsdu.apduSize.get());
            session.setPeerAsduSize((int) (long) respAsdu.asduSize.get());
            session.setPeerProtocolVersion((int) (long) respAsdu.protocolVersion.get());
        }

        return response;
    }

    /**
     * RPC - rpcCall - Service Code 0x72
     * Calls a remote procedure with request data.
     *
     * @param method  the method reference
     * @param reqData the request data payload
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu rpcCall(String method, CmsType<?> reqData) throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST)
                .method(method)
                .reqData(reqData);
        return send(asdu);
    }

    /**
     * RPC - rpcCall - Service Code 0x72 (continuation)
     * Continues a previous RPC call using a call ID from a previous response.
     *
     * @param method the method reference (must match the original call)
     * @param callId the call ID from a previous response's nextCallID
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu rpcCall(String method, byte[] callId) throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST)
                .method(method)
                .callID(callId);
        return send(asdu);
    }

    /**
     * RPC - getRpcMethodDefinition - Service Code 0x71
     * Retrieves definitions for the specified RPC methods.
     *
     * @param references the method references to look up
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getRpcMethodDefinition(String... references) throws Exception {
        CmsGetRpcMethodDefinition asdu = new CmsGetRpcMethodDefinition(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addReference(ref);
        }
        return send(asdu);
    }

    /**
     * RPC - getRpcInterfaceDefinition - Service Code 0x70
     * Retrieves the definition of all methods in the specified interface.
     *
     * @param interfaceName the interface name (e.g. "IF1")
     * @param referenceAfter optional method name to continue from a previous response
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getRpcInterfaceDefinition(String interfaceName, String referenceAfter) throws Exception {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.REQUEST)
                .interfaceName(interfaceName);
        if (referenceAfter != null && !referenceAfter.isEmpty()) {
            asdu.referenceAfter(referenceAfter);
        }
        return send(asdu);
    }

    public CmsApdu getRpcInterfaceDefinition(String interfaceName) throws Exception {
        return getRpcInterfaceDefinition(interfaceName, null);
    }

    /**
     * RPC - getRpcMethodDirectory - Service Code 0x6F
     * Retrieves the list of method names for the specified interface.
     * If interfaceName is null or empty, returns methods from all interfaces.
     *
     * @param interfaceName  the interface name (optional, null for all)
     * @param referenceAfter optional method name to continue from a previous response
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getRpcMethodDirectory(String interfaceName, String referenceAfter) throws Exception {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.REQUEST);
        if (interfaceName != null && !interfaceName.isEmpty()) {
            asdu.interfaceName(interfaceName);
        }
        if (referenceAfter != null && !referenceAfter.isEmpty()) {
            asdu.referenceAfter(referenceAfter);
        }
        return send(asdu);
    }

    public CmsApdu getRpcMethodDirectory(String interfaceName) throws Exception {
        return getRpcMethodDirectory(interfaceName, null);
    }

    public CmsApdu getRpcMethodDirectory() throws Exception {
        return getRpcMethodDirectory(null, null);
    }

    /**
     * RPC - getRpcInterfaceDirectory - Service Code 0x6E
     * Retrieves the list of all available RPC interfaces.
     *
     * @param referenceAfter optional interface name to continue from a previous response
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getRpcInterfaceDirectory(String referenceAfter) throws Exception {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST);
        if (referenceAfter != null && !referenceAfter.isEmpty()) {
            asdu.referenceAfter(referenceAfter);
        }
        return send(asdu);
    }

    public CmsApdu getRpcInterfaceDirectory() throws Exception {
        return getRpcInterfaceDirectory(null);
    }

    /**
     * File - getFile - Service Code 0x80
     * Reads a file chunk from the server at the specified start position.
     * Repeatedly call with increasing startPosition to read the full file.
     * Set startPosition to 0 to cancel the read.
     *
     * @param fileName      the file path starting with "/"
     * @param startPosition the 1-based start position, or 0 to cancel
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getFile(String fileName, long startPosition) throws Exception {
        CmsGetFile asdu = new CmsGetFile(MessageType.REQUEST)
                .fileName(fileName)
                .startPosition(startPosition);
        return send(asdu);
    }

    /**
     * File - setFile - Service Code 0x81
     * Writes a file chunk to the server. First chunk should use startPosition=1.
     * Set endOfFile=true on the last chunk. Set startPosition=0 to cancel.
     *
     * @param fileName      the file path starting with "/"
     * @param startPosition the 1-based start position, or 0 to cancel
     * @param fileData      the data chunk to write
     * @param endOfFile     true if this is the final chunk
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu setFile(String fileName, long startPosition, byte[] fileData, boolean endOfFile) throws Exception {
        CmsSetFile asdu = new CmsSetFile(MessageType.REQUEST)
                .fileName(fileName)
                .startPosition(startPosition)
                .fileData(fileData)
                .endOfFile(endOfFile);
        return send(asdu);
    }

    /**
     * File - deleteFile - Service Code 0x82
     * Deletes a file on the server. Protected builtin files cannot be deleted.
     *
     * @param fileName the file path starting with "/"
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu deleteFile(String fileName) throws Exception {
        CmsDeleteFile asdu = new CmsDeleteFile(MessageType.REQUEST)
                .fileName(fileName);
        return send(asdu);
    }

    /**
     * File - getFileAttributeValues - Service Code 0x83
     * Retrieves the attributes (size, last modified, checksum) of a file.
     *
     * @param fileName the file path starting with "/"
     * @return the response APDU containing file attributes
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getFileAttributeValues(String fileName) throws Exception {
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.REQUEST)
                .fileName(fileName);
        return send(asdu);
    }

    /**
     * File - getFileDirectory - Service Code 0x84
     * Lists files in a directory, optionally filtered by path, time range, and pagination.
     *
     * @param pathName   the directory path (e.g. "/" or "/data"), null for all
     * @param fileAfter  optional filename to continue pagination from a previous response
     * @return the response APDU containing file entries
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getFileDirectory(String pathName, String fileAfter) throws Exception {
        CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.REQUEST);
        if (pathName != null && !pathName.isEmpty()) {
            asdu.pathName(pathName);
        }
        if (fileAfter != null && !fileAfter.isEmpty()) {
            asdu.fileAfter(fileAfter);
        }
        return send(asdu);
    }

    public CmsApdu getFileDirectory(String pathName) throws Exception {
        return getFileDirectory(pathName, null);
    }

    public CmsApdu getFileDirectory() throws Exception {
        return getFileDirectory(null, null);
    }

    /**
     * Control - select - Service Code 0x44
     * Selects a controllable object prior to an operate command.
     *
     * @param reference the object reference to select
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu select(String reference) throws Exception {
        CmsSelect asdu = new CmsSelect(MessageType.REQUEST)
                .reference(reference);
        return send(asdu);
    }

    /**
     * Control - selectWithValue - Service Code 0x45
     * Selects a controllable object with the intended control value.
     *
     * @param reference the object reference
     * @param ctlVal    the control value
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu selectWithValue(String reference, com.ysh.dlt2811bean.datatypes.type.CmsType<?> ctlVal) throws Exception {
        CmsSelectWithValue asdu = new CmsSelectWithValue(MessageType.REQUEST)
                .reference(reference)
                .ctlVal(ctlVal)
                .ctlNum(0)
                .test(false);
        return send(asdu);
    }

    /**
     * Control - operate - Service Code 0x47
     * Executes a control command on a previously selected object.
     *
     * @param reference the object reference
     * @param ctlVal    the control value
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu operate(String reference, com.ysh.dlt2811bean.datatypes.type.CmsType<?> ctlVal) throws Exception {
        CmsOperate asdu = new CmsOperate(MessageType.REQUEST)
                .reference(reference)
                .ctlVal(ctlVal)
                .ctlNum(1)
                .test(false);
        return send(asdu);
    }

    /**
     * Control - cancel - Service Code 0x46
     * Cancels a previously issued Select, SelectWithValue, or Operate command.
     *
     * @param reference the object reference to cancel
     * @param ctlVal    the control value
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu cancel(String reference, com.ysh.dlt2811bean.datatypes.type.CmsType<?> ctlVal) throws Exception {
        CmsCancel asdu = new CmsCancel(MessageType.REQUEST)
                .reference(reference)
                .ctlVal(ctlVal)
                .ctlNum(2)
                .test(false);
        return send(asdu);
    }

    /**
     * Control - commandTermination - Service Code 0x48
     * Handles command termination notification from server.
     *
     * @param reference the object reference
     * @param ctlVal    the control value
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu commandTermination(String reference, com.ysh.dlt2811bean.datatypes.type.CmsType<?> ctlVal) throws Exception {
        CmsCommandTermination asdu = new CmsCommandTermination(MessageType.REQUEST_POSITIVE)
                .reference(reference)
                .ctlVal(ctlVal)
                .ctlNum(3)
                .test(false);
        return send(asdu);
    }

    /**
     * Control - timeActivatedOperate - Service Code 0x49
     * Executes a control operation at a specific time.
     *
     * @param reference the object reference
     * @param ctlVal    the control value
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu timeActivatedOperate(String reference, com.ysh.dlt2811bean.datatypes.type.CmsType<?> ctlVal) throws Exception {
        CmsTimeActivatedOperate asdu = new CmsTimeActivatedOperate(MessageType.REQUEST)
                .reference(reference)
                .ctlVal(ctlVal)
                .ctlNum(4)
                .test(false);
        return send(asdu);
    }

    /**
     * Control - timeActivatedOperateTermination - Service Code 0x4A
     * Terminates a scheduled time-activated control operation.
     *
     * @param reference the object reference
     * @param ctlVal    the control value
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsApdu timeActivatedOperateTermination(String reference, com.ysh.dlt2811bean.datatypes.type.CmsType<?> ctlVal) throws Exception {
        CmsTimeActivatedOperateTermination asdu = new CmsTimeActivatedOperateTermination(MessageType.REQUEST_POSITIVE)
                .reference(reference)
                .ctlVal(ctlVal)
                .ctlNum(5)
                .test(false);
        return send(asdu);
    }

    /**
     * SV - getMSVCBValues - Service Code 0x69
     * Retrieves multicast sampled value control block values.
     *
     * @param references one or more MSVCB references
     * @return the response APDU
     * @throws Exception if not connected or timeout
     */
    public CmsApdu getMSVCBValues(String... references) throws Exception {
        CmsGetMSVCBValues asdu = new CmsGetMSVCBValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addReference(ref);
        }
        return send(asdu);
    }

    /**
     * SV - setMSVCBValues - Service Code 0x6A
     * Sets multicast sampled value control block values.
     *
     * @param entries one or more MSVCB entries to set
     * @return the response APDU
     * @throws Exception if not connected or timeout
     */
    public CmsApdu setMSVCBValues(CmsSetMSVCBValuesEntry... entries) throws Exception {
        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.REQUEST);
        for (CmsSetMSVCBValuesEntry entry : entries) {
            asdu.addMsvcb(entry);
        }
        return send(asdu);
    }

    /**
     * GOOSE - getGoCBValues - Service Code 0x66
     */
    public CmsApdu getGoCBValues(String... references) throws Exception {
        CmsGetGoCBValues asdu = new CmsGetGoCBValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addGocbReference(ref);
        }
        return send(asdu);
    }

    /**
     * GOOSE - setGoCBValues - Service Code 0x67
     */
    public CmsApdu setGoCBValues(CmsSetGoCBValuesEntry... entries) throws Exception {
        CmsSetGoCBValues asdu = new CmsSetGoCBValues(MessageType.REQUEST);
        for (CmsSetGoCBValuesEntry entry : entries) {
            asdu.addGocb(entry);
        }
        return send(asdu);
    }

    /**
     * LOG - getLCBValues - Service Code 0x5F
     */
    public CmsApdu getLCBValues(String... references) throws Exception {
        CmsGetLCBValues asdu = new CmsGetLCBValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addReference(ref);
        }
        return send(asdu);
    }

    /**
     * LOG - setLCBValues - Service Code 0x60
     */
    public CmsApdu setLCBValues(CmsSetLCBValues asdu) throws Exception {
        return send(asdu);
    }

    /**
     * LOG - queryLogByTime - Service Code 0x61
     */
    public CmsApdu queryLogByTime(String logReference) throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.REQUEST)
                .logReference(logReference);
        return send(asdu);
    }

    public CmsApdu queryLogByTime(String logReference, byte[] entryAfter) throws Exception {
        CmsQueryLogByTime asdu = new CmsQueryLogByTime(MessageType.REQUEST)
                .logReference(logReference)
                .entryAfter(entryAfter);
        return send(asdu);
    }

    /**
     * LOG - queryLogAfter - Service Code 0x62
     */
    public CmsApdu queryLogAfter(String logReference, byte[] entryId) throws Exception {
        CmsQueryLogAfter asdu = new CmsQueryLogAfter(MessageType.REQUEST)
                .logReference(logReference)
                .entry(entryId);
        return send(asdu);
    }

    /**
     * LOG - getLogStatusValues - Service Code 0x63
     */
    public CmsApdu getLogStatusValues(String... references) throws Exception {
        CmsGetLogStatusValues asdu = new CmsGetLogStatusValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addLogReference(ref);
        }
        return send(asdu);
    }

    /**
     * REPORT - report - Service Code 0x5A
     */
    public void report(CmsReport asdu) throws Exception {
        doSendWithoutResponse(asdu);
    }

    /**
     * REPORT - getBRCBValues - Service Code 0x5B
     */
    public CmsApdu getBRCBValues(String... references) throws Exception {
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addBrcbReference(ref);
        }
        return send(asdu);
    }

    /**
     * REPORT - setBRCBValues - Service Code 0x5C
     */
    public CmsApdu setBRCBValues(CmsSetBRCBValues asdu) throws Exception {
        return send(asdu);
    }

    /**
     * REPORT - getURCBValues - Service Code 0x5D
     */
    public CmsApdu getURCBValues(String... references) throws Exception {
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addReference(ref);
        }
        return send(asdu);
    }

    /**
     * REPORT - setURCBValues - Service Code 0x5E
     */
    public CmsApdu setURCBValues(CmsSetURCBValues asdu) throws Exception {
        return send(asdu);
    }

    /**
     * SETTING - selectActiveSG - Service Code 0x54
     */
    public CmsApdu selectActiveSG(String sgcbReference, int sgNum) throws Exception {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.REQUEST)
                .sgcbReference(sgcbReference)
                .settingGroupNumber(sgNum);
        return send(asdu);
    }

    /**
     * SETTING - selectEditSG - Service Code 0x55
     */
    public CmsApdu selectEditSG(String sgcbReference, int sgNum) throws Exception {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.REQUEST)
                .sgcbReference(sgcbReference)
                .settingGroupNumber(sgNum);
        return send(asdu);
    }

    /**
     * SETTING - setEditSGValue - Service Code 0x56
     */
    public CmsApdu setEditSGValue(CmsSetEditSGValue asdu) throws Exception {
        return send(asdu);
    }

    /**
     * SETTING - confirmEditSGValues - Service Code 0x57
     */
    public CmsApdu confirmEditSGValues(String sgcbReference) throws Exception {
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.REQUEST)
                .sgcbReference(sgcbReference);
        return send(asdu);
    }

    /**
     * SETTING - getEditSGValue - Service Code 0x58
     */
    public CmsApdu getEditSGValue(String reference, String fc) throws Exception {
        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.REQUEST)
                .addData(reference, fc);
        return send(asdu);
    }

    /**
     * SETTING - getSGCBValues - Service Code 0x59
     */
    public CmsApdu getSGCBValues(String... references) throws Exception {
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.REQUEST);
        for (String ref : references) {
            asdu.addSgcbReference(ref);
        }
        return send(asdu);
    }

    /**
     * DATASET - getDataSetValues - Service Code 0x3A
     */
    public CmsApdu getDataSetValues(String datasetReference) throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST)
                .datasetReference(datasetReference);
        return send(asdu);
    }

    public CmsApdu getDataSetValues(String datasetReference, String referenceAfter) throws Exception {
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST)
                .datasetReference(datasetReference)
                .referenceAfter(referenceAfter);
        return send(asdu);
    }

    /**
     * DATASET - setDataSetValues - Service Code 0x3B
     */
    public CmsApdu setDataSetValues(CmsSetDataSetValues asdu) throws Exception {
        return send(asdu);
    }

    /**
     * DATASET - createDataSet - Service Code 0x36
     */
    public CmsApdu createDataSet(String datasetReference, String memberReference, String fc) throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference(datasetReference)
                .addMemberData(memberReference, fc);
        return send(asdu);
    }

    public CmsApdu createDataSet(String datasetReference, String referenceAfter, String memberReference, String fc) throws Exception {
        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference(datasetReference)
                .referenceAfter(referenceAfter)
                .addMemberData(memberReference, fc);
        return send(asdu);
    }

    /**
     * DATASET - deleteDataSet - Service Code 0x37
     */
    public CmsApdu deleteDataSet(String datasetReference) throws Exception {
        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.REQUEST)
                .datasetReference(datasetReference);
        return send(asdu);
    }

    /**
     * DATASET - getDataSetDirectory - Service Code 0x39
     */
    public CmsApdu getDataSetDirectory(String datasetReference) throws Exception {
        CmsGetDataSetDirectory asdu = new CmsGetDataSetDirectory(MessageType.REQUEST)
                .datasetReference(datasetReference);
        return send(asdu);
    }

    public CmsApdu test() throws Exception {
        CmsTest asdu = new CmsTest(MessageType.REQUEST);
        return testEcho(asdu);
    }
}
