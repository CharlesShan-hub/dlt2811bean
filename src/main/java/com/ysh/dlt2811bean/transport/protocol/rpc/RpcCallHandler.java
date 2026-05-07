package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsRpcCall;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for RpcCall service (SC=0x72).
 */
public class RpcCallHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.RPC_CALL;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsRpcCall asdu = (CmsRpcCall) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
