package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class CmsClient {

    private static final long DEFAULT_TIMEOUT_MS = 5000;

    private final String host;
    private final int port;
    private final ConcurrentHashMap<Integer, PendingRequest> pendingRequests = new ConcurrentHashMap<>();
    private final AtomicInteger nextReqId = new AtomicInteger(1);

    public CmsClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private CmsConnection connection;
    private volatile byte[] associationId;
    private volatile boolean tcpConnected;

    // ==================== Connection ====================

    public void run(boolean blocking) throws IOException {
        CmsClientTransport clientTransport = new CmsClientTransport();
        connection = clientTransport.connect(host, port, new CmsTransportListener() {
            @Override
            public void onConnected(CmsConnection conn) {
                tcpConnected = true;
                System.out.println("[Client] Connected to " + host + ":" + port);
            }

            @Override
            public void onApduReceived(CmsConnection conn, CmsApdu apdu) {
                dispatchResponse(conn, apdu);
            }

            @Override
            public void onDisconnected(CmsConnection conn) {
                tcpConnected = false;
                associationId = null;
                System.out.println("[Client] Disconnected");
            }

            @Override
            public void onError(CmsConnection conn, Exception e) {
                System.err.println("[Client] Error: " + e.getMessage());
            }
        });
        connection.startReadLoop();
        System.out.println("[Client] Running");
        if (blocking) {
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ==================== Association Management ====================

    /**
     * Associate with the server (no authentication parameter).
     *
     * @param serverAccessPoint server access point reference, e.g. "IED1.AP1"
     * @return the server-assigned 64-byte association ID
     * @throws Exception if the association is refused or times out
     */
    public byte[] associate(String serverAccessPoint) throws Exception {
        return associate(serverAccessPoint, (AuthenticationParameter) null);
    }

    /**
     * Associate with the server.
     *
     * @param serverAccessPoint server access point reference, e.g. "IED1.AP1"
     * @param authParam        optional authentication parameter (may be null)
     * @return the server-assigned 64-byte association ID
     * @throws Exception if the association is refused or times out
     */
    public byte[] associate(String serverAccessPoint, AuthenticationParameter authParam) throws Exception {
        if (!tcpConnected) {
            throw new IllegalStateException("Client is not connected");
        }

        int reqId = nextReqId.getAndIncrement();
        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .reqId(reqId)
                .serverAccessPointReference(serverAccessPoint, "");
        if (authParam != null) {
            asdu.authenticationParameter(authParam);
        }

        CountDownLatch latch = new CountDownLatch(1);
        AssociateResult result = new AssociateResult();
        pendingRequests.put(reqId, new PendingRequest(latch, result));

        CmsApdu request = new CmsApdu(asdu, MessageType.REQUEST);
        connection.send(request);
        System.out.println("[Client] Associate request sent: " + serverAccessPoint);

        boolean ok = latch.await(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        pendingRequests.remove(reqId);

        if (!ok) {
            throw new TimeoutException("Associate timed out after " + DEFAULT_TIMEOUT_MS + "ms");
        }
        if (result.error != null) {
            throw new Exception("Association refused: " + result.error.get());
        }

        associationId = result.associationId;
        System.out.println("[Client] Associated, associationId=" + hex(result.associationId));
        return result.associationId;
    }

    /**
     * Release the association gracefully.
     *
     * @throws Exception if refused or timed out
     */
    public void release() throws Exception {
        checkAssociated();
        byte[] id = associationId;

        int reqId = nextReqId.getAndIncrement();
        CmsRelease asdu = new CmsRelease(MessageType.REQUEST)
                .reqId(reqId)
                .associationId(id);

        CountDownLatch latch = new CountDownLatch(1);
        ReleaseResult result = new ReleaseResult();
        pendingRequests.put(reqId, new PendingRequest(latch, result));

        connection.send(new CmsApdu(asdu, MessageType.REQUEST));
        System.out.println("[Client] Release request sent");

        boolean ok = latch.await(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        pendingRequests.remove(reqId);

        if (!ok) {
            throw new TimeoutException("Release timed out after " + DEFAULT_TIMEOUT_MS + "ms");
        }
        if (result.error != null) {
            throw new Exception("Release refused: " + result.error.get());
        }

        associationId = null;
        System.out.println("[Client] Released");
    }

    /**
     * Abort the association.
     *
     * @param reasonCode the abort reason code (0..5)
     * @throws Exception if timed out
     * @see AbortReason#OTHER, AbortReason#UNRECOGNIZED_SERVICE, etc.
     */
    public void abort(int reasonCode) throws Exception {
        int reqId = nextReqId.getAndIncrement();
        CmsAbort asdu = new CmsAbort(MessageType.REQUEST)
                .reqId(reqId)
                .reason(reasonCode);

        CountDownLatch latch = new CountDownLatch(1);
        pendingRequests.put(reqId, new PendingRequest(latch, null));

        connection.send(new CmsApdu(asdu, MessageType.REQUEST));
        System.out.println("[Client] Abort request sent: " + reasonCode);

        boolean ok = latch.await(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        pendingRequests.remove(reqId);

        if (!ok) {
            throw new TimeoutException("Abort timed out after " + DEFAULT_TIMEOUT_MS + "ms");
        }

        associationId = null;
        System.out.println("[Client] Aborted");
    }

    // ==================== Send ====================

    /**
     * Send an APDU to the server. The association ID is NOT injected automatically —
     * callers must set it on the ASDU themselves (e.g. CmsRelease, CmsAbort).
     */
    public void send(CmsApdu apdu) throws IOException {
        if (connection == null || !connection.isConnected()) {
            throw new IOException("Client is not connected");
        }
        ServiceName svc = apdu.getAsdu().getServiceName();
        if (svc != ServiceName.ASSOCIATE && svc != ServiceName.TEST) {
            if (associationId == null) {
                throw new IllegalStateException("Client is not associated");
            }
        }
        connection.send(apdu);
    }

    // ==================== State ====================

    /**
     * @return true if the application-level association has been established
     */
    public boolean isAssociated() {
        return associationId != null;
    }

    public byte[] getAssociationId() {
        return associationId;
    }

    private void checkAssociated() {
        if (!tcpConnected) {
            throw new IllegalStateException("Client is not connected");
        }
        if (associationId == null) {
            throw new IllegalStateException("Client is not associated");
        }
    }

    // ==================== Response Dispatch ====================

    private void dispatchResponse(CmsConnection conn, CmsApdu apdu) {
        int reqId = apdu.getAsdu().reqId().get();
        PendingRequest pending = pendingRequests.remove(reqId);

        if (pending != null) {
            handlePendingResponse(apdu, pending);
        } else {
            // Not a pending request — delegate to subclass/service handler
            handleIncoming(conn, apdu);
        }
    }

    private void handlePendingResponse(CmsApdu apdu, PendingRequest pending) {
        ServiceName svc = apdu.getAsdu().getServiceName();
        if (svc == ServiceName.ASSOCIATE) {
            CmsAssociate asdu = (CmsAssociate) apdu.getAsdu();
            if (apdu.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                ((AssociateResult) pending.result).associationId = asdu.associationId().get();
            } else {
                ((AssociateResult) pending.result).error = asdu.serviceError();
            }
        } else if (svc == ServiceName.RELEASE) {
            if (apdu.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
                CmsRelease asdu = (CmsRelease) apdu.getAsdu();
                ReleaseResult rr = new ReleaseResult();
                rr.error = asdu.serviceError();
                pending.result = rr;
            }
        }
        // Abort responses are one-way; Associate/Release responses counted here
        pending.latch.countDown();
    }

    // ==================== Service Handler (for non-pending APDUs) ====================

    protected void handleIncoming(CmsConnection conn, CmsApdu apdu) {
        System.out.println("[Client] Received: " + apdu.getAsdu());
        ServiceName svc = apdu.getAsdu().getServiceName();
        switch (svc) {
            case TEST:
                onTestReceived(conn, apdu);
                break;
            default:
                onServiceReceived(conn, apdu, svc);
                break;
        }
    }

    protected void onTestReceived(CmsConnection conn, CmsApdu apdu) {
        // No reply needed for TEST
    }

    protected void onServiceReceived(CmsConnection conn, CmsApdu apdu, ServiceName svc) {
        // TODO: implement service-specific handling
    }

    // ==================== Lifecycle ====================

    public void close(long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        if (connection != null) {
            connection.close();
        }
    }

    public void close() {
        if (connection != null) {
            connection.close();
        }
    }

    // ==================== Internal Helpers ====================

    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, 8); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        if (bytes.length > 8) sb.append("...");
        return sb.toString();
    }

    // ==================== Inner Classes ====================

    private static class PendingRequest {
        final CountDownLatch latch;
        Object result;

        PendingRequest(CountDownLatch latch, Object result) {
            this.latch = latch;
            this.result = result;
        }
    }

    private static class AssociateResult {
        byte[] associationId;
        CmsServiceError error;
    }

    private static class ReleaseResult {
        CmsServiceError error;
    }
}
