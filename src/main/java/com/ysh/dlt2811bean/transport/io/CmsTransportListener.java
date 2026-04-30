package com.ysh.dlt2811bean.transport.io;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

/**
 * Callback interface for transport layer events.
 *
 * <p>All methods are called from I/O threads. Keep implementations fast
 * and avoid blocking. For heavy processing, dispatch to a separate thread pool.
 */
public interface CmsTransportListener {

    /**
     * Called when a connection is established.
     *
     * @param connection the established connection
     */
    void onConnected(CmsConnection connection);

    /**
     * Called when an APDU is received.
     *
     * @param connection the connection that received the APDU
     * @param apdu       the received APDU
     */
    void onApduReceived(CmsConnection connection, CmsApdu apdu);

    /**
     * Called when the connection is closed (by either side).
     *
     * @param connection the closed connection
     */
    void onDisconnected(CmsConnection connection);

    /**
     * Called when an I/O error occurs.
     *
     * @param connection the connection with error (may be null if accept failed)
     * @param e         the exception
     */
    void onError(CmsConnection connection, Exception e);
}
