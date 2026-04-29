package com.ysh.dlt2811bean.transport;

import java.io.IOException;
import java.net.Socket;

public class CmsClientTransport {

    public CmsConnection connect(String host, int port, CmsTransportListener listener) throws IOException {
        Socket socket = new Socket(host, port);
        return new CmsConnection(socket, listener);
    }
}
