package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * A single TCP connection for CMS protocol.
 *
 * <p>Provides APDU-level send/receive over a socket connection.
 * The read loop runs in a background thread started by {@link #startReadLoop()}.
 *
 * <p>Thread safety: multiple threads may call {@link #send(CmsApdu)} concurrently.
 * The read loop is single-threaded.
 */
public class CmsConnection {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final CmsTransportListener listener;

    private volatile boolean running;

    /**
     * Creates a connection over an existing socket.
     *
     * @param socket    the socket
     * @param listener  event listener
     * @throws IOException if streams cannot be obtained
     */
    public CmsConnection(Socket socket, CmsTransportListener listener) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.listener = listener;
        this.running = true;
    }

    /**
     * Sends an APDU over the connection.
     *
     * <p>The APDU is encoded and prefixed with a 4-byte length field (big-endian).
     *
     * @param apdu the APDU to send
     * @throws IOException if the send fails
     */
    public void send(CmsApdu apdu) throws IOException {
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        byte[] bytes = pos.toByteArray();
        synchronized (dos) {
            dos.writeInt(bytes.length);
            dos.write(bytes);
            dos.flush();
        }
    }

    /**
     * Receives an APDU from the connection (blocking).
     *
     * @return the received APDU
     * @throws Exception if receive fails or connection is closed
     */
    public CmsApdu receive() throws Exception {
        int length = dis.readInt();
        if (length < 0) {
            throw new EOFException("Connection closed by peer");
        }
        byte[] buf = new byte[length];
        dis.readFully(buf);
        return new CmsApdu().decode(new PerInputStream(buf));
    }

    /**
     * Starts the background read loop.
     *
     * <p>Incoming APDUs are dispatched to {@link CmsTransportListener#onApduReceived}.
     * When the connection closes or an error occurs, the appropriate callback is invoked.
     */
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
        }, "cms-conn-" + socket.getPort());
        reader.setDaemon(true);
        reader.start();
    }

    /**
     * Closes the connection and releases resources.
     */
    public void close() {
        running = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * @return true if the connection is open
     */
    public boolean isConnected() {
        return running && socket.isConnected() && !socket.isClosed();
    }

    /**
     * @return the underlying socket
     */
    public Socket getSocket() {
        return socket;
    }
}
