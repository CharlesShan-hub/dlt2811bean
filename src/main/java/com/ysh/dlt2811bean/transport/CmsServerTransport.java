package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class CmsServerTransport {

    private final int port;
    private final CmsTransportListener listener;
    private final CopyOnWriteArrayList<CmsConnection> connections = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private volatile boolean running;

    public CmsServerTransport(int port, CmsTransportListener listener) {
        this.port = port;
        this.listener = listener;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        Thread acceptor = new Thread(this::acceptLoop, "cms-acceptor");
        acceptor.setDaemon(true);
        acceptor.start();
    }

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

    private void acceptLoop() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                CmsConnection conn = new CmsConnection(socket, new CmsTransportListener() {
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
                });
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
}
