package com.ysh.jcms.utils.transport.session;

/**
 * Session state lifecycle.
 *
 * <pre>
 *   DISCONNECTED → CONNECTED → ASSOCIATED
 * </pre>
 */
public enum SessionState {
    DISCONNECTED, CONNECTED, ASSOCIATED
}
