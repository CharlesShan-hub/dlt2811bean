package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionError;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionRequest;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionResponse;
import com.ysh.jcms.core.data.sequence.rpc.CmsRpcMethodEntry;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetRpcInterfaceDefinitionServer
        extends
            BaseServerHandler<CmsGetRpcInterfaceDefinitionRequest, CmsGetRpcInterfaceDefinitionError> {
    public GetRpcInterfaceDefinitionServer() {
        super(CmsServiceInfo.GET_RPC_INTERFACE_DEFINITION, CmsGetRpcInterfaceDefinitionRequest.class,
                CmsGetRpcInterfaceDefinitionError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetRpcInterfaceDefinitionRequest req, int reqId) {
        String iface = str(req.interfaceName);
        if (iface == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        RpcRegistry.InterfaceDef def = RpcRegistry.getInterface(iface);
        if (def == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        log.info("GetRpcInterfaceDefinition from {}: interface={}", session.sessionId(), iface);
        CmsGetRpcInterfaceDefinitionResponse resp = new CmsGetRpcInterfaceDefinitionResponse();
        for (String methodName : def.methods.keySet()) {
            CmsRpcMethodEntry entry = RpcRegistry.buildMethodEntry(iface, methodName);
            if (entry != null)
                resp.method.add(entry);
        }
        resp.moreFollows(false);
        log.info("GetRpcInterfaceDefinition: returning {} method(s)", resp.method.size());
        return ok(resp, reqId);
    }
}
