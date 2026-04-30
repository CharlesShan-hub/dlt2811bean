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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Tracks in-flight segmented frames by ReqID.
     * Key: ReqID (from first 2 bytes of ASDU)
     * Value: accumulated segments (all with isNext=true except the last)
     */
    private final Map<Integer, List<CmsApdu>> pending = new HashMap<>();

    private volatile boolean running;

    /**
     * Creates a connection over an existing socket.
     *
     * @param socket   the socket
     * @param listener event listener
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
     * <p>If the ASDU exceeds MAX_ASDU_SIZE, the APDU is automatically split
     * into multiple frames (Next flag set on all but the last).
     *
     * @param apdu the APDU to send
     * @throws IOException if the send fails
     */
    public void send(CmsApdu apdu) throws IOException {
        List<CmsApdu> segments = apdu.split();
        synchronized (dos) {
            for (CmsApdu seg : segments) {
                sendOne(seg);
            }
        }
    }

    /**
     * Sends a single (already-segmented) APDU frame.
     */
    private void sendOne(CmsApdu apdu) throws IOException {
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        byte[] bytes = pos.toByteArray();
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();
    }

    /**
     * Reads the next complete APDU from the connection (blocking).
     *
     * <p>Handles segmented frames by accumulating all segments with the same
     * ReqID before returning. Multiple concurrent requests are supported
     * (interleaved frames are reassembled independently by ReqID).
     *
     * <p>When a new frame arrives with a ReqID that already has pending segments,
     * the pending group is cleared (protocol violation: ReqID reused before completion).
     *
     * @return the complete APDU (merged if segmented)
     * @throws Exception if receive fails or connection is closed
     */
    public CmsApdu receive() throws Exception {
        CmsApdu seg = loadSegment();
        int reqId = seg.getReqId();

        // Protocol violation: ReqID reused before completion
        if (pending.containsKey(reqId)) {
            pending.remove(reqId);
            throw new IllegalStateException("ReqID " + reqId + " reused before previous transfer completed");
        }

        List<CmsApdu> segments = new ArrayList<>();
        segments.add(seg);

        // Keep reading while More flag is set
        while (seg.getApch().isNext()) {
            seg = loadSegment();
            if (seg.getReqId() != reqId) {
                // Clear the pending group for this ReqID
                pending.remove(reqId);
                throw new IllegalStateException(
                    "Segment ReqID mismatch: expected " + reqId + ", got " + seg.getReqId());
            }
            segments.add(seg);
        }

        // Merge if segmented, otherwise decode ASDU directly
        if (segments.size() > 1) {
            CmsApdu last = segments.removeLast();
            last.merge(segments);
        } else {
            segments.getFirst().decodeAsdu();
        }
        return segments.getFirst();
    }

    private CmsApdu loadSegment() throws Exception {
        int length = dis.readInt();
        if (length < 0) {
            throw new EOFException("Connection closed by peer");
        }
        byte[] buf = new byte[length];
        dis.readFully(buf);
        return new CmsApdu().load(new PerInputStream(buf));
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
