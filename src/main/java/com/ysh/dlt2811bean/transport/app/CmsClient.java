package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.transport.io.CmsClientTransport;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import com.ysh.dlt2811bean.transport.io.CmsTransportListener;
import com.ysh.dlt2811bean.transport.session.CmsClientSession;
import com.ysh.dlt2811bean.transport.session.PendingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
public class CmsClient {

    private static final Logger log = LoggerFactory.getLogger(CmsClient.class);

    private final CmsClientTransport transport = new CmsClientTransport();
    private final ClientListener listener = new ClientListener();
    private volatile CmsConnection connection;
    private volatile CmsClientSession session;

    // 默认访问点配置
    private String defaultAp = "CLIENT";
    private String defaultEp = "EP1";

    // ==================== Connection ====================

    /**
     * Connects to a CMS server.
     *
     * @param host server hostname or IP
     * @param port server port
     * @throws Exception if connection fails
     */
    public void connect(String host, int port) throws Exception {
        connection = transport.connect(host, port, listener);
        session = new CmsClientSession(connection);
        connection.startReadLoop();
        log.info("Connected to {}:{}", host, port);
    }

    /**
     * Closes the connection.
     */
    public void close() {
        if (connection != null) {
            connection.close();
            connection = null;
            session = null;
            log.info("Connection closed");
        }
    }

    // ==================== Config ====================

    /**
     * Sets the default server access point for associate.
     *
     * @param ap the AP name (e.g., "AP1")
     * @param ep the EP name (e.g., "EP1")
     * @return this client for chaining
     */
    public CmsClient setAccessPoint(String ap, String ep) {
        this.defaultAp = ap;
        this.defaultEp = ep;
        return this;
    }

    // ==================== Association Services (Public API) ====================

    /**
     * Sends an Associate request to establish a connection.
     *
     * @return the response APDU (positive or negative)
     * @throws Exception if not connected or timeout
     */
    public CmsApdu associate() throws Exception {
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference(defaultAp, defaultEp);
        return send(asdu);
    }
    public CmsApdu associate(String ap, String ep) throws Exception {
        setAccessPoint(ap, ep);
        return associate();
    }

    /**
     * Sends a Release request to gracefully close the association.
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
     * Sends an Abort request to immediately terminate the connection.
     * Abort is one-way: no response expected, connection will be closed.
     * Default reason is OTHER (0).
     *
     * @throws Exception if send fails
     */
    public void abort() throws Exception {
        abort(0);
    }

    /**
     * Sends an Abort request with specified reason to immediately terminate the connection.
     * Abort is one-way: no response expected, connection will be closed.
     *
     * @param reason the abort reason (0-5, see {@link com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason})
     * @throws Exception if send fails
     */
    public void abort(int reason) throws Exception {
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST).reason(reason);
        doSendWithoutResponse(asdu);
        close();
    }

    /**
     * Sends a Test request and waits for the server echo.
     *
     * @return the echoed response APDU, or null if timeout
     * @throws Exception if not connected
     */
    public CmsApdu test() throws Exception {
        CmsTest asdu = new CmsTest(MessageType.REQUEST);
        return testEcho(asdu);
    }

    // ==================== Internal Echo ====================

    /**
     * Internal: sends Test and waits for echo.
     */
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

    /**
     * Internal send: assigns ReqID and waits for response.
     */
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

    /**
     * Internal send: no response expected (one-way).
     */
    private void doSendWithoutResponse(CmsAsdu<?> asdu) throws Exception {
        int reqId = session.nextReqId();
        asdu.reqId(reqId);
        CmsApdu request = new CmsApdu(asdu);
        session.send(request);
        log.debug("[ReqID={}] Sent {} (one-way)", reqId, asdu.getClass().getSimpleName());
    }

    /**
     * @return true if connected
     */
    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    /**
     * @return the current association ID, or null if not associated
     */
    public byte[] getAssociationId() {
        return session != null ? session.getAssociationId() : null;
    }

    /**
     * Sets the association ID (typically set after a successful Associate response).
     */
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
}
