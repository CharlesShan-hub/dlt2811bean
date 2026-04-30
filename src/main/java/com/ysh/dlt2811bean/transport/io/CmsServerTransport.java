package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-side transport that listens on a port and accepts incoming connections.
 *
 * <p>Each accepted connection is wrapped in a {@link CmsConnection} and notified
 * via {@link CmsTransportListener#onConnected}. The listener is also notified of
 * disconnections and errors.
 */
public class CmsServerTransport {

    private final int port;
    private final CmsTransportListener listener;
    private final CopyOnWriteArrayList<CmsConnection> connections = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private volatile boolean running;

    /**
     * Creates a server transport.
     *
     * @param port     the port to listen on
     * @param listener event listener for all connections
     */
    public CmsServerTransport(int port, CmsTransportListener listener) {
        this.port = port;
        this.listener = listener;
    }

    /**
     * Starts listening for connections.
     *
     * @throws IOException if the port cannot be bound
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        Thread acceptor = new Thread(this::acceptLoop, "cms-acceptor");
        acceptor.setDaemon(true);
        acceptor.start();
    }

    /**
     * Stops the server and closes all connections.
     */
    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
        for (CmsConnection conn : connections) {
            conn.close();
        }
        connections.clear();
    }

    /**
     * @return true if the server is running
     */
    public boolean isRunning() {
        return running;
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                CmsConnection conn = new CmsConnection(socket, wrapListener(socket));
                connections.add(conn);
                listener.onConnected(conn);
                conn.startReadLoop();
            } catch (IOException e) {
                if (running) {
                    listener.onError(null, e);
                }
            }
        }
    }

    private CmsTransportListener wrapListener(Socket socket) {
        return new CmsTransportListener() {
            @Override
            public void onConnected(CmsConnection connection) {
                listener.onConnected(connection);
            }

            @Override
            public void onApduReceived(CmsConnection connection, CmsApdu apdu) {
                listener.onApduReceived(connection, apdu);
            }

            @Override
            public void onDisconnected(CmsConnection connection) {
                connections.remove(connection);
                listener.onDisconnected(connection);
            }

            @Override
            public void onError(CmsConnection connection, Exception e) {
                listener.onError(connection, e);
            }
        };
    }
}
