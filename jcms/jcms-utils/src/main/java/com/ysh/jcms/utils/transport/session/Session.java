package com.ysh.jcms.utils.transport.session;

import com.ysh.jcms.utils.transport.wire.Connection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Session — abstract base for a logical CMS conversation over a
 * {@link Connection}. Lifecycle follows DL/T 2811 8.2 association services:
 * CONNECTED (after TCP) → ASSOCIATED (after Associate) → DISCONNECTED
 * (Release/Abort/TCP loss).
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class Session {

    private final String sessionId;
    private final Connection connection;
    @Setter(AccessLevel.NONE)
    private volatile SessionState state = SessionState.DISCONNECTED;
    private volatile byte[] associationId;
    private volatile String associatedApRef;
    private volatile boolean associatedSecure;

    private volatile boolean negotiated;
    private volatile int negotiatedApduSize = 65535;

    /**
     * Sole state entry point. Manual (release/abort) and passive (TCP disconnect)
     * transitions both go through here; cleanup is dispatched from the old -> new
     * state pair.
     */
    public void state(SessionState newState) {
        SessionState old = this.state;
        this.state = newState;
        if (old == newState)
            return;
        if (old == SessionState.ASSOCIATED) {
            clearAssociation(); // hook 1: leaving associated
        }
        if (newState == SessionState.DISCONNECTED) {
            clearConnection(); // hook 2: session teardown
        }
    }

    /**
     * Hook 1: clear association-level state when leaving ASSOCIATED. Subclasses may
     * override to add business state (setting groups, reports).
     */
    protected void clearAssociation() {
        this.associationId = null;
        this.associatedApRef = null;
        this.associatedSecure = false;
    }

    /**
     * Hook 2: full teardown when entering DISCONNECTED. Idempotent; repeated
     * transitions are skipped by the old==new guard in {@link #state}.
     */
    protected void clearConnection() {
        clearAssociation();
        this.negotiated = false;
        this.negotiatedApduSize = 65535;
        if (connection != null) {
            connection.close();
        }
    }

    protected Session(String sessionId, Connection connection) {
        this.sessionId = sessionId;
        this.connection = connection;
    }

    public boolean connected() {
        return connection != null && connection.connected();
    }

    public boolean isAssociated() {
        return state == SessionState.ASSOCIATED && associationId != null;
    }
}
