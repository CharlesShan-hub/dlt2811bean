package com.ysh.dlt2811bean.transport1;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CmsServer {

    private final CmsServerTransport transport;
    /** Maps each connection to its associated ID (null = not associated yet). */
    private final ConcurrentHashMap<CmsConnection, byte[]> associationMap = new ConcurrentHashMap<>();

    public CmsServer(int port) {
        transport = new CmsServerTransport(port, new CmsTransportListener() {
            @Override
            public void onConnected(CmsConnection conn) {
                System.out.println("[Server] Client connected: " + conn.getSocket().getRemoteSocketAddress());
            }

            @Override
            public void onApduReceived(CmsConnection conn, CmsApdu apdu) {
                System.out.println("[Server] Received: " + apdu.getAsdu());
                handleIncoming(conn, apdu);
            }

            @Override
            public void onDisconnected(CmsConnection conn) {
                associationMap.remove(conn);
                System.out.println("[Server] Client disconnected: " + conn.getSocket().getRemoteSocketAddress());
            }

            @Override
            public void onError(CmsConnection conn, Exception e) {
                System.err.println("[Server] Error: " + e.getMessage());
            }
        });
    }

    // ==================== Dispatch ====================

    protected void handleIncoming(CmsConnection conn, CmsApdu apdu) {
        ServiceName svc = apdu.getAsdu().getServiceName();
        switch (svc) {
            case ASSOCIATE:
                onAssociateReceived(conn, apdu);
                break;
            case RELEASE:
                onReleaseReceived(conn, apdu);
                break;
            case ABORT:
                onAbortReceived(conn, apdu);
                break;
            case TEST:
                onTestReceived(conn, apdu);
                break;
            default:
                onServiceReceived(conn, apdu, svc);
                break;
        }
    }

    // ==================== Associate ====================

    protected void onAssociateReceived(CmsConnection conn, CmsApdu apdu) {
        CmsAssociate asdu = (CmsAssociate) apdu.getAsdu();

        // TODO: validate serverAccessPointReference against server's model
        // For now, always accept
        byte[] assocId = AssociationIdGenerator.generate();
        associationMap.put(conn, assocId);

        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(assocId)
                .serviceError(CmsServiceError.NO_ERROR);

        try {
            conn.send(new CmsApdu(response, MessageType.RESPONSE_POSITIVE));
            System.out.println("[Server] Association accepted, associationId=" + hex(assocId));
        } catch (Exception e) {
            System.err.println("[Server] Failed to send Associate response: " + e.getMessage());
        }
    }

    // ==================== Release ====================

    protected void onReleaseReceived(CmsConnection conn, CmsApdu apdu) {
        CmsRelease asdu = (CmsRelease) apdu.getAsdu();
        byte[] localId = associationMap.get(conn);

        // Release always responds positively (even if not associated)
        CmsRelease response = new CmsRelease(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(localId != null ? localId : new byte[64])
                .serviceError(CmsServiceError.NO_ERROR);

        try {
            conn.send(new CmsApdu(response, MessageType.RESPONSE_POSITIVE));
            associationMap.remove(conn);
            System.out.println("[Server] Association released");
        } catch (Exception e) {
            System.err.println("[Server] Failed to send Release response: " + e.getMessage());
        }
    }

    // ==================== Abort ====================

    protected void onAbortReceived(CmsConnection conn, CmsApdu apdu) {
        CmsAbort asdu = (CmsAbort) apdu.getAsdu();
        associationMap.remove(conn);
        System.out.println("[Server] Association aborted: " + asdu.reason().get());
        // Abort is one-way; do not reply
    }

    // ==================== Test ====================

    protected void onTestReceived(CmsConnection conn, CmsApdu apdu) {
        try {
            CmsTest resp = new CmsTest();
            CmsApdu response = new CmsApdu(resp, MessageType.RESPONSE_POSITIVE);
            conn.send(response);
            System.out.println("[Server] Test replied");
        } catch (Exception e) {
            System.err.println("[Server] Test reply error: " + e.getMessage());
        }
    }

    // ==================== Other Services ====================

    protected void onServiceReceived(CmsConnection conn, CmsApdu apdu, ServiceName svc) {
        // TODO: implement service-specific handling
    }

    // ==================== Lifecycle ====================

    public void run(boolean blocking) throws IOException {
        transport.start();
        System.out.println("[Server] Listening on port " + transport);
        if (blocking) {
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop(long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        transport.stop();
    }

    public void stop() {
        transport.stop();
    }

    // ==================== Helpers ====================

    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, 8); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        if (bytes.length > 8) sb.append("...");
        return sb.toString();
    }
}
