package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.rpc.CmsGetRpcInterfaceDefinitionError;
import com.ysh.jcms.svc.rpc.CmsGetRpcInterfaceDefinitionRequest;
import com.ysh.jcms.svc.rpc.CmsGetRpcInterfaceDefinitionResponse;
import com.ysh.jcms.svc.rpc.CmsRpcMethodEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRpcInterfaceDefinitionServer extends BaseServerHandler {
    private static final Logger log = LoggerFactory.getLogger(GetRpcInterfaceDefinitionServer.class);
    public GetRpcInterfaceDefinitionServer() {
        super(ServiceName.GET_RPC_INTERFACE_DEFINITION, CmsGetRpcInterfaceDefinitionRequest.class, CmsGetRpcInterfaceDefinitionError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetRpcInterfaceDefinitionRequest req = (CmsGetRpcInterfaceDefinitionRequest) rawReq;
        String iface = str(req.interfaceName);
        if (iface == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        RpcRegistry.InterfaceDef def = RpcRegistry.getInterface(iface);
        if (def == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        log.info("GetRpcInterfaceDefinition from {}: interface={}", session.getSessionId(), iface);
        CmsGetRpcInterfaceDefinitionResponse resp = new CmsGetRpcInterfaceDefinitionResponse().reqId(reqId);
        for (String methodName : def.methods.keySet()) {
            CmsRpcMethodEntry entry = RpcRegistry.buildMethodEntry(iface, methodName);
            if (entry != null)
                resp.method.add(entry);
        }
        resp.moreFollows(false);
        log.info("GetRpcInterfaceDefinition: returning {} method(s)", resp.method.count);
        return ok(resp, reqId);
    }
}
