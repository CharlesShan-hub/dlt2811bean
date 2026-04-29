package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;

import java.io.IOException;

public class CmsClient {

    private final String host;
    private final int port;
    private CmsConnection connection;

    public CmsClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connect to the remote server and start the read loop.
     *
     * @param blocking if {@code true}, block the calling thread indefinitely (useful for
     *                 standalone processes where the connection must stay alive in foreground);
     *                 if {@code false}, return immediately so the caller can continue.
     */
    public void run(boolean blocking) throws IOException {
        CmsClientTransport clientTransport = new CmsClientTransport();
        connection = clientTransport.connect(host, port, new CmsTransportListener() {
            @Override
            public void onConnected(CmsConnection conn) {
                System.out.println("[Client] Connected to " + host + ":" + port);
            }

            @Override
            public void onApduReceived(CmsConnection conn, CmsApdu apdu) {
                System.out.println("[Client] Received: " + apdu.getAsdu());
                handleIncoming(conn, apdu);
            }

            @Override
            public void onDisconnected(CmsConnection conn) {
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

    /**
     * Dispatch incoming APDU by service type.
     * Subclasses may override specific handlers.
     */
    protected void handleIncoming(CmsConnection conn, CmsApdu apdu) {
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
        // need to do nothing
    }

    protected void onServiceReceived(CmsConnection conn, CmsApdu apdu, ServiceName svc) {
        // TODO: implement service-specific handling
    }

    public void send(CmsApdu apdu) throws IOException {
        if (connection == null || !connection.isConnected()) {
            throw new IOException("Client is not connected");
        }
        connection.send(apdu);
    }

    public void close(long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        if (connection != null) {
            connection.close();
        }
    }

    /** Close immediately without delay. */
    public void close() {
        if (connection != null) {
            connection.close();
        }
    }
}