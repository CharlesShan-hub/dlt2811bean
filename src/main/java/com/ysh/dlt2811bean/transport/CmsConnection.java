package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class CmsConnection {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final CmsTransportListener listener;

    private volatile boolean running;

    public CmsConnection(Socket socket, CmsTransportListener listener) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.listener = listener;
        this.running = true;
    }

    public void send(CmsApdu apdu) throws IOException {
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        byte[] bytes = pos.toByteArray();
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();
    }

    public CmsApdu receive() throws Exception {
        int length = dis.readInt();
        if (length < 0) {
            throw new EOFException("Connection closed by peer");
        }
        byte[] buf = new byte[length];
        dis.readFully(buf);
        return new CmsApdu().decode(new PerInputStream(buf));
    }

    public void startReadLoop() {
        Thread reader = new Thread(() -> {
            try {
                while (running) {
                    CmsApdu apdu = receive();
                    listener.onApduReceived(this, apdu);
                }
            } catch (EOFException | SocketException e) {
                // normal disconnection
            } catch (Exception e) {
                if (running) {
                    listener.onError(this, e);
                }
            } finally {
                close();
                listener.onDisconnected(this);
            }
        }, "cms-connection-" + socket.getPort());
        reader.setDaemon(true);
        reader.start();
    }

    public void close() {
        running = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public boolean isConnected() {
        return running && socket.isConnected() && !socket.isClosed();
    }

    public Socket getSocket() {
        return socket;
    }
}
