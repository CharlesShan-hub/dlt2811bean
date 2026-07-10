package com.ysh.jcms.utils.transport.wire;

import com.ysh.jcms.utils.transport.frame.Frame;

/**
 * Listener for connection lifecycle events.
 *
 * <p>
 * All callbacks are invoked from I/O threads. Keep implementations fast.
 */
public interface ConnectionListener {

    /** A new TCP connection is established. */
    void onConnected(Connection connection);

    /** A complete Frame has been received. */
    void onFrameReceived(Connection connection, Frame frame);

    /** The connection has been closed. */
    void onDisconnected(Connection connection);

    /** An I/O error occurred. */
    void onError(Connection connection, Exception e);
}
