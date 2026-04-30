package com.ysh.dlt2811bean.transport.io;

import java.io.IOException;
import java.net.Socket;

/**
 * Client-side transport factory.
 *
 * <p>Creates {@link CmsConnection} instances by connecting to a server.
 */
public class CmsClientTransport {

    /**
     * Connects to a CMS server.
     *
     * @param host     server hostname or IP address
     * @param port     server port
     * @param listener event listener for the new connection
     * @return a connected CmsConnection
     * @throws IOException if the connection fails
     */
    public CmsConnection connect(String host, int port, CmsTransportListener listener) throws IOException {
        Socket socket = new Socket(host, port);
        return new CmsConnection(socket, listener);
    }
}
