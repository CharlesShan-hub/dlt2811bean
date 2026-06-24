package com.ysh.jcms.utils.transport.session;

/**
 * Session state lifecycle.
 *
 * <pre>
 *   DISCONNECTED → CONNECTED → ASSOCIATED ⇄ RELEASING
 *                                  ↓
 *                             ABORTED / TIMEOUT
 * </pre>
 */
public enum SessionState {
    DISCONNECTED,
    CONNECTED,
    ASSOCIATED,
    RELEASING
}
