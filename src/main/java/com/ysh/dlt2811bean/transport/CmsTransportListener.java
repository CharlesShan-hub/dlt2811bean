package com.ysh.dlt2811bean.transport;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

public interface CmsTransportListener {

    void onConnected(CmsConnection connection);

    void onApduReceived(CmsConnection connection, CmsApdu apdu);

    void onDisconnected(CmsConnection connection);

    void onError(CmsConnection connection, Exception e);
}
