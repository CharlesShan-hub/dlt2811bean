package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.svc.rpc.*;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRpcMethodDefinitionServer extends BaseServerHandler {
    private static final Logger log = LoggerFactory.getLogger(GetRpcMethodDefinitionServer.class);
    public GetRpcMethodDefinitionServer() {
        super(ServiceName.GET_RPC_METHOD_DEFINITION, CmsGetRpcMethodDefinitionRequest.class, CmsGetRpcMethodDefinitionError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetRpcMethodDefinitionRequest req = (CmsGetRpcMethodDefinitionRequest) rawReq;
        log.info("GetRpcMethodDefinition from {}: {} refs", session.getSessionId(), req.reference.count);
        CmsGetRpcMethodDefinitionResponse resp = new CmsGetRpcMethodDefinitionResponse().reqId(reqId);
        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            RpcRegistry.MethodDef def = RpcRegistry.getMethodByRef(ref);
            CmsRpcMethodDefChoice choice = new CmsRpcMethodDefChoice();
            if (def != null) {
                choice.choice(CmsRpcMethodDefChoice.METHOD);
                choice.altMethod.timeout.value(def.timeout);
                choice.altMethod.version.value(def.version);
                choice.altMethod.request = def.requestDef;
                choice.altMethod.response = def.responseDef;
            } else {
                choice.choice(CmsRpcMethodDefChoice.ERROR);
                choice.altError.value(12); // TYPE_CONFLICT
            }
            resp.reference.add(choice);
        }
        return ok(resp, reqId);
    }
}
