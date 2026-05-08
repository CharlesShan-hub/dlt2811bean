package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsControlCode;

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

    /** 连续收到的错误帧计数器，超阈值会主动断开连接 */
    private int consecutiveErrors;
    /** 连续错误阈值，超过此值主动中断连接 */
    private static final int MAX_CONSECUTIVE_ERRORS = 5;

    /**
     * Marker exception for frame format errors (PI/SC/FL) that should NOT
     * cause a disconnect — the frame is discarded and the connection stays alive.
     */
    public static class FrameFormatException extends Exception {
        public FrameFormatException(String message) {
            super(message);
        }
    }

    /**
     * Creates a connection over an existing socket.
     *
     * @param socket   the socket
     * @param listener event listener
     * @throws IOException if streams cannot be obtained
     */
    public CmsConnection(Socket socket, CmsTransportListener listener) throws IOException {
        this.socket = socket;
        try {
            socket.setKeepAlive(true);
        } catch (Exception ignored) {
            // TCP KeepAlive is best-effort; some platforms may not support it
        }
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

    private void sendOne(CmsApdu apdu) throws IOException {
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);
        byte[] bytes = pos.toByteArray();
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
     * @throws FrameFormatException if the frame has format errors (frame is discarded)
     * @throws Exception if receive fails or connection is closed
     */
    public CmsApdu receive() throws Exception {
        CmsApdu seg = loadSegment();
        int reqId = seg.getReqId();

        if (pending.containsKey(reqId)) {
            pending.remove(reqId);
            throw new FrameFormatException("ReqID " + reqId + " reused before previous transfer completed");
        }

        List<CmsApdu> segments = new ArrayList<>();
        segments.add(seg);

        while (seg.getApch().isNext()) {
            seg = loadSegment();
            if (seg.getReqId() != reqId) {
                pending.remove(reqId);
                throw new FrameFormatException(
                    "Segment ReqID mismatch: expected " + reqId + ", got " + seg.getReqId());
            }
            segments.add(seg);
        }

        if (segments.size() > 1) {
            CmsApdu last = segments.removeLast();
            last.merge(segments);
        } else {
            segments.getFirst().decodeAsdu();
        }
        return segments.getFirst();
    }

    private CmsApdu loadSegment() throws Exception {
        byte[] apchBuf = new byte[4];
        dis.readFully(apchBuf);

        // 6.1.1 / 6.1.2: Validate Protocol Identifier (PI) — CC byte bits 3-0 must be 0x01
        int pi = apchBuf[0] & 0x0F;
        if (pi != CmsControlCode.PI_DEFAULT) {
            throw new FrameFormatException(
                "Invalid PI: expected " + CmsControlCode.PI_DEFAULT + ", got " + pi);
        }

        // Validate Frame Length (FL) — must be ≤ 65531 (MAX_ASDU_SIZE)
        int fl = ((apchBuf[2] & 0xFF) << 8) | (apchBuf[3] & 0xFF);
        if (fl < 0 || fl > CmsApdu.MAX_ASDU_SIZE) {
            throw new FrameFormatException(
                "Invalid FL: " + fl + ", max allowed is " + CmsApdu.MAX_ASDU_SIZE);
        }

        // Validate Service Code (SC) — must be a known service code
        int sc = apchBuf[1] & 0xFF;
        if (ServiceName.fromInt(sc) == null) {
            throw new FrameFormatException(
                "Unknown service code: 0x" + Integer.toHexString(sc));
        }

        byte[] asduBuf = new byte[fl];
        if (fl > 0) {
            dis.readFully(asduBuf);
        }

        byte[] fullFrame = new byte[4 + fl];
        System.arraycopy(apchBuf, 0, fullFrame, 0, 4);
        System.arraycopy(asduBuf, 0, fullFrame, 4, fl);

        return new CmsApdu().load(new PerInputStream(fullFrame));
    }

    /**
     * Starts the background read loop.
     *
     * <p>Incoming APDUs are dispatched to {@link CmsTransportListener#onApduReceived}.
     * When the connection closes or an error occurs, the appropriate callback is invoked.
     *
     * <p>Frame format errors (PI/SC/FL) are handled per 6.1.3:
     * <ul>
     *   <li>The malformed frame is discarded (not processed)</li>
     *   <li>The connection is NOT closed</li>
     *   <li>If consecutive errors exceed the threshold, the connection is closed</li>
     * </ul>
     */
    public void startReadLoop() {
        Thread reader = new Thread(() -> {
            try {
                while (running) {
                    try {
                        CmsApdu apdu = receive();
                        consecutiveErrors = 0;
                        listener.onApduReceived(this, apdu);
                    } catch (FrameFormatException e) {
                        consecutiveErrors++;
                        if (listener != null) {
                            listener.onError(this, e);
                        }
                        if (consecutiveErrors >= MAX_CONSECUTIVE_ERRORS) {
                            break;
                        }
                    }
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
     * @return true if this is a secure (TLS/SSL) connection
     */
    public boolean isSecure() {
        return socket instanceof javax.net.ssl.SSLSocket;
    }

    /**
     * @return the underlying socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @return the remote IP address string (e.g. "192.168.1.100"), or null if closed
     */
    public String getRemoteAddress() {
        try {
            return socket.getInetAddress().getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }
}
