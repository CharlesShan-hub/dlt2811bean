package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;

import java.io.IOException;

public class CmsServer {

    private final CmsServerTransport transport;

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
                System.out.println("[Server] Client disconnected: " + conn.getSocket().getRemoteSocketAddress());
            }

            @Override
            public void onError(CmsConnection conn, Exception e) {
                System.err.println("[Server] Error: " + e.getMessage());
            }
        });
    }

    /**
     * Dispatch incoming APDU by service type.
     * Subclasses may override specific handlers.
     */
    protected void handleIncoming(CmsConnection conn, CmsApdu apdu) {
        ServiceName svc = apdu.getAsdu().getServiceName();
        switch (svc) {
            case ASSOCIATE:
                onAssociateReceived(conn, apdu);
                break;
            case TEST:
                onTestReceived(conn, apdu);
                break;
            default:
                onServiceReceived(conn, apdu, svc);
                break;
        }
    }

    protected void onAssociateReceived(CmsConnection conn, CmsApdu apdu) {
        try {
            CmsTest resp = new CmsTest();
            CmsApdu response = new CmsApdu(resp, MessageType.RESPONSE_POSITIVE);
            conn.send(response);
            System.out.println("[Server] Replied: " + resp);
        } catch (Exception e) {
            System.err.println("[Server] Reply error: " + e.getMessage());
        }
    }

    protected void onTestReceived(CmsConnection conn, CmsApdu apdu) {
        try {
            CmsTest resp = new CmsTest();
            CmsApdu response = new CmsApdu(resp, MessageType.RESPONSE_POSITIVE);
            conn.send(response);
            System.out.println("[Server] Replied: " + resp);
        } catch (Exception e) {
            System.err.println("[Server] Reply error: " + e.getMessage());
        }
    }

    protected void onServiceReceived(CmsConnection conn, CmsApdu apdu, ServiceName svc) {
        // TODO: implement service-specific handling
    }

    /**
     * Start the server.
     *
     * @param blocking if {@code true}, block the calling thread indefinitely (useful for
     *                 standalone processes where user input keeps the server alive);
     *                 if {@code false}, return immediately so the caller can continue.
     */
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

    /** Stop immediately without delay. */
    public void stop() {
        transport.stop();
    }
}