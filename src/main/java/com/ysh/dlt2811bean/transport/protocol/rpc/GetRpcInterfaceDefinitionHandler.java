package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDefinition;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetRpcInterfaceDefinition service (SC=0x70).
 */
public class GetRpcInterfaceDefinitionHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_INTERFACE_DEFINITION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetRpcInterfaceDefinition asdu = (CmsGetRpcInterfaceDefinition) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
