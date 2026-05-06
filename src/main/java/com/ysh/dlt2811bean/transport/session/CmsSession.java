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

    protected CmsSession(String sessionId, CmsConnection connection) {
        this.sessionId = sessionId;
        this.connection = connection;
    }

    // ==================== State ====================

    public SessionState getState() {
        return state;
    }

    public void setState(SessionState state) {
        this.state = state;
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

    public CmsConnection getConnection() {
        return connection;
    }

    // ==================== I/O ====================

    public void send(com.ysh.dlt2811bean.service.protocol.types.CmsApdu apdu) throws Exception {
        if (!isConnected()) {
            throw new IllegalStateException("Session is not connected");
        }
        connection.send(apdu);
    }

    // ==================== Lifecycle ====================

    /** Called when the underlying connection is closed. */
    public void onDisconnected() {
        this.state = SessionState.CLOSED;
        this.associationId = null;
    }

    @Override
    public String toString() {
        return sessionId + "[" + state + "]";
    }
}
