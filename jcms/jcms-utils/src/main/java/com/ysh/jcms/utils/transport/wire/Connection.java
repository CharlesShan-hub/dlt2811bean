package com.ysh.jcms.utils.transport.wire;

import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameAssembler;
import com.ysh.jcms.utils.transport.frame.FrameCodec;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Connection — a single TCP connection for the CMS protocol.
 *
 * <p>
 * Owns the socket and a reader thread that reads frames from the input stream.
 * Complete frames are reassembled by {@link FrameAssembler} and delivered to
 * the {@link ConnectionListener}.
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Connection {

    private final Socket socket;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final DataInputStream dis;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final DataOutputStream dos;
    private final ConnectionListener listener;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final FrameAssembler assembler = new FrameAssembler();

    /**
     * Max accepted frame length (FL): ReqID(2) + payload, per DL/T 2811 (FL max
     * 65531).
     */
    private static final int MAX_FRAME_LENGTH = Frame.MAX_PAYLOAD_SIZE + FrameCodec.REQID_SIZE;
    /**
     * Disconnect after this many consecutive malformed frames, per DL/T 2811 6.1.3.
     */
    private static final int MAX_CONSECUTIVE_ERRORS = 5;
    /**
     * Per-connection read buffer; larger buffer means fewer syscalls for big
     * frames.
     */
    private static final int INPUT_BUFFER_SIZE = 64 * 1024;

    @Getter(AccessLevel.NONE)
    private volatile int maxFrameSize = MAX_FRAME_LENGTH;
    @Getter(AccessLevel.NONE)
    private volatile int peerAsduSize = Frame.MAX_PAYLOAD_SIZE;
    @Getter(AccessLevel.NONE)
    private volatile boolean fragmentationSupported = true;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile boolean running;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Thread readerThread;
    /**
     * Consecutive malformed frames received; disconnects when it exceeds the limit.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int errorCount;
    /** Optional callback fired once when the connection is torn down. */
    @Getter(AccessLevel.NONE)
    private volatile Runnable onClosed;

    public Connection(Socket socket, ConnectionListener listener) throws IOException {
        this.socket = socket;
        try {
            socket.setKeepAlive(true); // DL/T 2811 6.9.3: enable TCP keepalive
            socket.setTcpNoDelay(true); // low latency for interactive request/response
        } catch (Exception ignored) {
        }
        this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream(), INPUT_BUFFER_SIZE));
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.listener = listener;
        this.running = true;
    }

    /** Start the reader thread. */
    public void startReader() {
        readerThread = new Thread(this::readLoop, "cms-reader-" + socket.getPort());
        readerThread.setDaemon(true);
        readerThread.start();
    }

    private void readLoop() {
        try {
            while (running && !socket.isClosed()) {
                Frame frame = readFrame();
                if (frame != null) {
                    listener.onFrameReceived(this, frame);
                }
            }
        } catch (EOFException e) {
            // normal close by peer
        } catch (Exception e) {
            if (running)
                listener.onError(this, e);
        } finally {
            running = false;
            listener.onDisconnected(this);
            if (onClosed != null) {
                onClosed.run();
            }
        }
    }

    /**
     * Read a single frame from the wire. Format: [APCH:4][ReqID:2][Data:FL-2], APCH
     * = [CC][SC][FL(lo)][FL(hi)], FL = ReqID + Data (excludes APCH).
     */
    private Frame readFrame() throws IOException {
        byte[] header = new byte[FrameHeader.HEADER_SIZE];
        dis.readFully(header);
        FrameHeader fh = FrameHeader.decode(header, 0);

        int fl = fh.frameLength();
        if (fl < 0 || fl > maxFrameSize) {
            throw new IOException("Invalid frame length: " + fl); // 6.1.2 c) / 6.1.3: out-of-range FL
        }
        // DL/T 2811 6.1.3: wrong protocol type -> drop the frame; disconnect only after
        // repeated errors
        if (fh.pi() != FrameHeader.PI_DEFAULT) {
            skipFully(fl);
            if (++errorCount > MAX_CONSECUTIVE_ERRORS) {
                throw new IOException("Too many consecutive malformed frames");
            }
            return null;
        }
        errorCount = 0;

        int reqId = 0;
        byte[] data = new byte[0];
        if (fl >= FrameCodec.REQID_SIZE) { // header-only frames (Test) carry no ReqID
            byte[] reqIdBytes = new byte[FrameCodec.REQID_SIZE];
            dis.readFully(reqIdBytes);
            reqId = (reqIdBytes[0] & 0xFF) | ((reqIdBytes[1] & 0xFF) << 8); // DL/T 2811 6.2.1: ReqID is 16-bit little-endian
            data = new byte[fl - FrameCodec.REQID_SIZE]; // read the data directly, no extra copy
            dis.readFully(data);
        }

        Frame segment = new Frame(fh, data, reqId);
        try {
            return assembler.addSegment(segment);
        } catch (FrameAssembler.FrameFormatException e) {
            throw new IOException("Frame format error: " + e.getMessage());
        }
    }

    private void skipFully(int n) throws IOException {
        long remaining = n;
        while (remaining > 0) {
            long skipped = dis.skip(remaining);
            if (skipped <= 0) {
                throw new EOFException();
            }
            remaining -= skipped;
        }
    }

    /** Send a Frame over the connection. */
    public void send(Frame frame) throws IOException {
        // DL/T 2811 8.15.2 b): respect the peer's ASDU capability; never exceed it when
        // fragmentation is unsupported
        if (!fragmentationSupported && frame.asduBytes().length > peerAsduSize) {
            throw new IOException("Fragmentation not supported: ASDU size (" + frame.asduBytes().length + ") exceeds peer asduSize ("
                    + peerAsduSize + ")");
        }
        java.util.List<Frame> segments = FrameCodec.split(frame, peerAsduSize); // DL/T 2811 6.5.1: fragment when oversized
        synchronized (dos) {
            for (Frame seg : segments) {
                byte[] wire = FrameCodec.encode(seg);
                dos.write(wire);
            }
            dos.flush();
        }
    }

    public boolean connected() {
        return running && !socket.isClosed();
    }

    /** Close the connection. */
    public void close() {
        running = false;
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }
}
