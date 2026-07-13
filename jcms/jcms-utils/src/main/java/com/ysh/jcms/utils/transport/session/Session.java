package com.ysh.jcms.utils.transport.session;

import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.utils.transport.wire.Connection;
import lombok.Getter;
import lombok.Setter;

/**
 * Session — abstract base for a logical CMS conversation over a
 * {@link Connection}.
 */
@Getter
@Setter
public abstract class Session {

    private final String sessionId;
    private final Connection connection;
    private volatile SessionState state = SessionState.DISCONNECTED;
    private volatile byte[] associationId;

    private volatile boolean negotiated;
    private volatile int negotiatedApduSize = 65535;
    private volatile int peerAsduSize;
    private volatile int peerProtocolVersion;
    private volatile boolean fragmentationSupported = true;

    /**
     * Full cleanup: associationId and RCB runtime state. Subclasses may override to
     * add more.
     */
    public void clear() {
        this.associationId = null;
        this.fragmentationSupported = true;
        RcbStateManager.clear();
    }

    protected Session(String sessionId, Connection connection) {
        this.sessionId = sessionId;
        this.connection = connection;
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public boolean isAssociated() {
        return state == SessionState.ASSOCIATED && associationId != null;
    }
}
