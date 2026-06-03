package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.transport.io.CmsConnection;

/**
 * Base class for a CMS session.
 *
 * <p>A session represents a logical conversation over a {@link CmsConnection}.
 * Subclasses distinguish between client and server roles.
 *
 * <p>Session objects are NOT created directly; they are built by
 * {@link CmsServerSession} or {@link CmsClientSession}.
 */
public abstract class CmsSession {

    private final String sessionId;
    private final CmsConnection connection;
    private volatile SessionState state = SessionState.DISCONNECTED;
    private volatile byte[] associationId;

    private volatile boolean negotiated;
    private volatile int negotiatedApduSize = 65535;
    private volatile int peerAsduSize;
    private volatile int peerProtocolVersion;

    protected CmsSession(String sessionId, CmsConnection connection) {
        this.sessionId = sessionId;
        this.connection = connection;
    }

    // ==================== State ====================

    public SessionState getState() {
        return state;
    }

    public void setState(SessionState state) {
        SessionState old = this.state;
        this.state = state;
        if (state == SessionState.ASSOCIATED && old != SessionState.ASSOCIATED) {
            startKeepAlive();
        } else if (old == SessionState.ASSOCIATED && state != SessionState.ASSOCIATED) {
            stopKeepAlive();
        }
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public boolean isAssociated() {
        return state == SessionState.ASSOCIATED && associationId != null;
    }

    // ==================== Identity ====================

    public String getSessionId() {
        return sessionId;
    }

    public byte[] getAssociationId() {
        return associationId;
    }

    public void setAssociationId(byte[] associationId) {
        this.associationId = associationId;
    }

    /** Clears the association ID (e.g., on Release or Abort). */
    public void clearAssociationId() {
        this.associationId = null;
    }

    // ==================== Negotiation ====================

    public boolean isNegotiated() { return negotiated; }
    public void setNegotiated(boolean negotiated) { this.negotiated = negotiated; }

    public int getNegotiatedApduSize() { return negotiatedApduSize; }
    public void setNegotiatedApduSize(int negotiatedApduSize) {
        this.negotiatedApduSize = negotiatedApduSize;
        if (connection != null) {
            connection.setMaxFrameSize(negotiatedApduSize - 4);
        }
    }

    public int getPeerAsduSize() { return peerAsduSize; }
    public void setPeerAsduSize(int peerAsduSize) { this.peerAsduSize = peerAsduSize; }

    public int getPeerProtocolVersion() { return peerProtocolVersion; }
    public void setPeerProtocolVersion(int peerProtocolVersion) { this.peerProtocolVersion = peerProtocolVersion; }

    public CmsConnection getConnection() {
        return connection;
    }

    // ==================== KeepAlive ====================

    protected final KeepAliveManager keepAliveManager = new KeepAliveManager(this);

    public void startKeepAlive() {
        keepAliveManager.start();
    }

    public void stopKeepAlive() {
        keepAliveManager.stop();
    }

    /**
     * Called when any APDU is sent or received. Resets the idle timer.
     */
    public void onDataActivity() {
        keepAliveManager.onActivity();
    }

    // ==================== I/O ====================

    public void send(com.ysh.dlt2811bean.service.protocol.types.CmsApdu apdu) throws Exception {
        if (!isConnected()) {
            throw new IllegalStateException("Session is not connected");
        }
        onDataActivity();
        connection.send(apdu);
    }

    // ==================== Lifecycle ====================

    /** Called when the underlying connection is closed. */
    public void onDisconnected() {
        this.state = SessionState.CLOSED;
        this.associationId = null;
        stopKeepAlive();
    }

    @Override
    public String toString() {
        return sessionId + "[" + state + "]";
    }
}
