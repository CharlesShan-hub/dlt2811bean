package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetRpcMethodDirectory service (SC=0x6F).
 */
public class GetRpcMethodDirectoryHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_METHOD_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetRpcMethodDirectory asdu = (CmsGetRpcMethodDirectory) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
