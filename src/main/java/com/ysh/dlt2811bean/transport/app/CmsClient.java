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
 * <p>Provides high-level operations: connect, associate, release, send.
 * Handles ReqID assignment and response matching automatically.
 *
 * <p>Example:
 * <pre>
 * CmsClient client = new CmsClient();
 * client.connect("127.0.0.1", 8888);
 * client.associate(new CmsAssociate(MessageType.REQUEST).serverAccessPointReference("IED1", "AP1"));
 * // send other requests...
 * client.release();
 * client.close();
 * </pre>
 */
public class CmsClient {

    private static final Logger log = LoggerFactory.getLogger(CmsClient.class);

    private final CmsClientTransport transport = new CmsClientTransport();
    private final ClientListener listener = new ClientListener();
    private volatile CmsConnection connection;
    private volatile CmsClientSession session;

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

    // ==================== Association ====================

    /**
     * Sends an Associate request and waits for the response.
     *
     * @param asdu the Associate ASDU (messageType must be REQUEST)
     * @return the response APDU (positive or negative)
     * @throws Exception if the request fails or times out
     */
    public CmsApdu associate(CmsAssociate asdu) throws Exception {
        asdu.messageType(MessageType.REQUEST);
        return send(asdu);
    }

    /**
     * Sends a Release request and waits for the response.
     * On positive response, the association ID is cleared.
     *
     * @param asdu the Release ASDU (messageType must be REQUEST)
     * @return the response APDU (positive or negative)
     * @throws Exception if the request fails or times out
     */
    public CmsApdu release(CmsRelease asdu) throws Exception {
        asdu.messageType(MessageType.REQUEST);
        CmsApdu response = send(asdu);
        if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            setAssociationId(null);
        }
        return response;
    }

    // ==================== One-way services (no response expected) ====================

    /**
     * Sends an Abort request without waiting for a response.
     * Abort is one-way: the server does not reply.
     *
     * @param asdu the Abort ASDU (messageType must be REQUEST)
     */
    public void abort(CmsAbort asdu) throws Exception {
        if (session == null || !session.isConnected()) {
            throw new IllegalStateException("Not connected");
        }
        asdu.messageType(MessageType.REQUEST);
        int reqId = session.nextReqId();
        asdu.reqId(reqId);
        CmsApdu request = new CmsApdu(asdu);
        session.send(request);
        log.debug("[ReqID={}] Sent ABORT (one-way, no response)", reqId);
        // Close immediately — client initiates abort, server mirrors
        close();
    }

    // ==================== Test service (echo) ====================

    /**
     * Sends a Test request and waits for the server echo response.
     *
     * <p>Test has no ReqID and no ASDU — the server echoes an identical frame.
     * Since there is no ReqID to match, the response is collected via
     * {@link ClientListener#onApduReceived} after the request is sent.
     *
     * @param request the Test request
     * @return the echoed response APDU, or null if no response within timeout
     */
    public CmsApdu test(CmsTest request) throws Exception {
        if (session == null || !session.isConnected()) {
            throw new IllegalStateException("Not connected");
        }

        // Register pending request for Test echo
        PendingRequest pending = session.addPendingRequest(0);  // ReqID=0 for Test
        request.messageType(MessageType.REQUEST);
        CmsApdu apdu = new CmsApdu(request);
        session.send(apdu);
        log.debug("Sent TEST, waiting for echo...");

        // Wait for echo — will be completed by ClientListener.onApduReceived
        CmsApdu response = (CmsApdu) pending.await(session.getDefaultTimeoutMs());
        if (response == null) {
            session.removePendingRequest(0);
        }
        return response;
    }

    // ==================== Request/Response services ====================

    /**
     * Sends a service request and waits for the response.
     *
     * <p>The ReqID is automatically assigned from the session.
     * The response is matched by ReqID.
     *
     * <p>Special services:
     * <ul>
     *   <li>Abort — server does not reply, client closes connection</li>
     *   <li>Test  — server echoes the frame, wait for echo via {@link #test(CmsTest)}</li>
     * </ul>
     *
     * @param asdu the request ASDU (messageType must be REQUEST)
     * @return the response APDU, or null for Test
     * @throws Exception if not connected or timeout
     */
    public CmsApdu send(CmsAsdu<?> asdu) throws Exception {
        if (session == null || !session.isConnected()) {
            throw new IllegalStateException("Not connected");
        }

        ServiceName svc = asdu.getServiceName();

        // One-way: Abort has no response, send without pending request
        if (svc == ServiceName.ABORT) {
            abort((CmsAbort) asdu);
            return null;
        }

        // Test: server echoes the frame — delegate to test()
        if (svc == ServiceName.TEST) {
            return test((CmsTest) asdu);
        }

        int reqId = session.nextReqId();
        asdu.reqId(reqId);

        CmsApdu request = new CmsApdu(asdu);
        PendingRequest pending = session.addPendingRequest(reqId);

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
