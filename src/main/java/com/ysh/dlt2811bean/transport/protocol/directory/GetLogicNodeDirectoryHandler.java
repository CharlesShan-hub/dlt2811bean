package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;

/**
 * Handler for GetLogicNodeDirectory service (SC=0x52).
 */
public class GetLogicNodeDirectoryHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LOGIC_NODE_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        return new CmsApdu(request.getAsdu());
    }
}
