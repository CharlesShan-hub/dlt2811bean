package com.ysh.jcms.utils.transport.wire;

import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameAssembler;
import com.ysh.jcms.utils.transport.frame.FrameCodec;
import com.ysh.jcms.utils.transport.frame.FrameHeader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Connection — a single TCP connection for the CMS protocol.
 *
 * <p>
 * Owns the socket and a reader thread that reads frames from the input stream.
 * Complete frames are reassembled by {@link FrameAssembler} and delivered to
 * the {@link ConnectionListener}.
 */
public class Connection {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final ConnectionListener listener;
    private final FrameAssembler assembler = new FrameAssembler();

    /** Max accepted frame length (FL): APCH(4) + ReqID(2) + payload. */
    private static final int MAX_FRAME_LENGTH = Frame.MAX_PAYLOAD_SIZE + FrameHeader.HEADER_SIZE + FrameCodec.REQID_SIZE;

    private volatile int maxFrameSize = MAX_FRAME_LENGTH;
    private volatile int peerAsduSize = Frame.MAX_PAYLOAD_SIZE;
    private volatile boolean fragmentationSupported = true;
    private volatile boolean running;
    private Thread readerThread;

    public Connection(Socket socket, ConnectionListener listener) throws IOException {
        this.socket = socket;
        try {
            socket.setKeepAlive(true);
        } catch (Exception ignored) {
        }
        this.dis = new DataInputStream(socket.getInputStream());
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
        }
    }

    /**
     * Read a single frame from the wire. Format: [FL:2][APCH:4][ReqID:2][Data:FL-6]
     */
    private Frame readFrame() throws IOException {
        int fl = dis.readUnsignedShort();
        if (fl < FrameHeader.HEADER_SIZE + FrameCodec.REQID_SIZE || fl > maxFrameSize) {
            throw new IOException("Invalid frame length: " + fl);
        }

        byte[] header = new byte[FrameHeader.HEADER_SIZE];
        dis.readFully(header);
        FrameHeader fh = FrameHeader.decode(header, 0);

        byte[] asduBytes = new byte[fl - FrameHeader.HEADER_SIZE]; // includes ReqID
        dis.readFully(asduBytes);
        int reqId = (asduBytes[0] & 0xFF) | ((asduBytes[1] & 0xFF) << 8); // little-endian
        byte[] data = Arrays.copyOfRange(asduBytes, FrameCodec.REQID_SIZE, asduBytes.length);

        Frame segment = new Frame(fh, data, reqId);
        try {
            return assembler.addSegment(segment);
        } catch (FrameAssembler.FrameFormatException e) {
            throw new IOException("Frame format error: " + e.getMessage());
        }
    }

    /** Send a Frame over the connection. */
    public void send(Frame frame) throws IOException {
        // Reject if the peer cannot handle fragmentation and the ASDU exceeds its size
        if (!fragmentationSupported && frame.asduBytes().length > peerAsduSize) {
            throw new IOException("Fragmentation not supported: ASDU size (" + frame.asduBytes().length + ") exceeds peer asduSize ("
                    + peerAsduSize + ")");
        }
        java.util.List<Frame> segments = FrameCodec.split(frame, peerAsduSize);
        synchronized (dos) {
            for (Frame seg : segments) {
                byte[] wire = FrameCodec.encode(seg);
                dos.write(wire);
            }
            dos.flush();
        }
    }

    public Connection maxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return this;
    }
    public Connection peerAsduSize(int peerAsduSize) {
        this.peerAsduSize = peerAsduSize;
        return this;
    }
    public Connection fragmentationSupported(boolean fragmentationSupported) {
        this.fragmentationSupported = fragmentationSupported;
        return this;
    }
    public boolean connected() {
        return running && !socket.isClosed();
    }
    public Socket socket() {
        return socket;
    }
    public ConnectionListener listener() {
        return listener;
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
