package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDefinition;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetRpcMethodDefinition service (SC=0x71).
 */
public class GetRpcMethodDefinitionHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_METHOD_DEFINITION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetRpcMethodDefinition asdu = (CmsGetRpcMethodDefinition) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
