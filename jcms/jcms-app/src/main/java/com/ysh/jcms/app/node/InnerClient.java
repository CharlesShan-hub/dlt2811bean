package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.ClientSession;
import com.ysh.jcms.utils.transport.session.SessionState;
import com.ysh.jcms.utils.transport.wire.ClientConnector;
import com.ysh.jcms.utils.transport.wire.Connection;
import com.ysh.jcms.utils.transport.wire.ConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * InnerClient — pure sender.
 *
 * <p>
 * Sends requests and waits for responses synchronously. All business logic
 * (decoding, session updates) belongs to the caller. Push messages (Report,
 * CommandTermination) are handled by separate listeners.
 */
public class InnerClient implements ConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(InnerClient.class);

    private final ClientConnector connector = new ClientConnector();
    private volatile ClientSession session;
    private volatile Connection connection;
    private volatile String host;
    private volatile int port;

    private volatile Consumer<Frame> reportHandler;

    public InnerClient() {
    }

    /** Register a handler for incoming REPORT (unsolicited push) frames. */
    public void setReportHandler(Consumer<Frame> handler) {
        this.reportHandler = handler;
    }

    public void connect(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.connection = connector.connect(host, port, this);
        this.session = new ClientSession(connection);
        connection.startReader();
        log.info("Connected to {}:{}", host, port);
    }

    /**
     * Establish a TLS connection to the specified host and port.
     */
    public void connectTls(String host, int port, SSLContext sslContext) throws IOException {
        this.host = host;
        this.port = port;
        this.connection = connector.connectTls(host, port, this, sslContext);
        this.session = new ClientSession(connection);
        connection.startReader();
        log.info("TLS connected to {}:{}", host, port);
    }

    /**
     * Send a request and wait synchronously for the response. The reqId is
     * extracted from the first 2 bytes of the ASDU.
     *
     * @return the raw response Frame, or null on timeout
     */
    public Frame sendRequest(ServiceName serviceCode, byte[] asduBytes, long timeoutMs) throws IOException {
        if (session == null || !session.isConnected()) {
            throw new IOException("Not connected");
        }
        int reqId = extractReqId(asduBytes);
        session.addPendingRequest(reqId, timeoutMs);
        connection.send(new Frame(new FrameHeader().serviceCode(serviceCode).resp(false).err(false), asduBytes, reqId));
        try {
            return (Frame) session.waitForPendingRequest(reqId, timeoutMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private static int extractReqId(byte[] asdu) {
        if (asdu == null || asdu.length < 2)
            return 0;
        return ((asdu[0] & 0xFF) << 8) | (asdu[1] & 0xFF);
    }

    public Frame sendRequest(ServiceName serviceCode, byte[] asduBytes) throws IOException {
        return sendRequest(serviceCode, asduBytes, 5000);
    }

    public void close() {
        if (connection != null)
            connection.close();
        if (session != null)
            session.setState(SessionState.DISCONNECTED);
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }
    public ClientSession getSession() {
        return session;
    }
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void onConnected(Connection connection) {
    }

    @Override
    public void onFrameReceived(Connection connection, Frame frame) {
        try {
            if (session == null)
                return;
            // Match against pending request (response to a request we sent)
            if (session.tryDispatchResponse(frame))
                return;

            // Handle server-initiated TEST (keepalive probe)
            if (frame.header().serviceCode() == ServiceName.TEST && !frame.header().resp()) {
                try {
                    connection.send(
                            new Frame(new FrameHeader().serviceCode(ServiceName.TEST).resp(true).err(false), new byte[0], frame.reqId()));
                    log.debug("Responded to server TEST probe");
                } catch (IOException e) {
                    log.warn("Failed to respond to server TEST probe", e);
                }
                return;
            }

            // Handle server-pushed REPORT (unsolicited)
            if (frame.header().serviceCode() == ServiceName.REPORT && reportHandler != null) {
                try {
                    reportHandler.accept(frame);
                } catch (Exception e) {
                    log.warn("Report handler failed", e);
                }
                return;
            }
        } catch (Exception e) {
            log.warn("Unhandled exception in onFrameReceived: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onDisconnected(Connection connection) {
        if (session != null)
            session.setState(SessionState.DISCONNECTED);
        log.info("Disconnected from {}:{}", host, port);
    }

    @Override
    public void onError(Connection connection, Exception e) {
        log.error("Connection error", e);
    }
}
