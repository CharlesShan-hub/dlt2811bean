package com.ysh.dlt2811bean.transport.session;

/**
 * Session state lifecycle.
 *
 * <pre>
 *   DISCONNECTED ──connect()──► CONNECTED
 *        ▲                        │
 *        │                    negotiate()
 *        │                        │
 *        │                        ▼
 *        │                   ASSOCIATED ◄────► RELEASING
 *        │                        │
 *        │                   abort() / timeout
 *        │                        │
 *        └────────────────────────┘
 * </pre>
 */
public enum SessionState {

    /** TCP connection is not established. */
    DISCONNECTED,

    /** TCP connection established, waiting for association. */
    CONNECTED,

    /** Association completed, ready for services. */
    ASSOCIATED,

    /** Release in progress. */
    RELEASING,

    /** Aborted or timed out. */
    CLOSED
}
