package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.core.data.choice.CmsRpcMethodDefChoice;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.data.sequence.rpc.CmsRpcMethodDef;
import com.ysh.jcms.core.pdu.rpc.*;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetRpcMethodDefinitionServer extends BaseServerHandler<CmsGetRpcMethodDefinitionRequest, CmsGetRpcMethodDefinitionError> {
    public GetRpcMethodDefinitionServer() {
        super(CmsServiceInfo.GET_RPC_METHOD_DEFINITION, CmsGetRpcMethodDefinitionRequest.class, CmsGetRpcMethodDefinitionError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetRpcMethodDefinitionRequest req, int reqId) {
        log.info("GetRpcMethodDefinition from {}: {} refs", session.sessionId(), req.reference.size());
        CmsGetRpcMethodDefinitionResponse resp = new CmsGetRpcMethodDefinitionResponse();
        for (CmsString refObj : req.reference) {
            String ref = refObj.value();
            RpcRegistry.MethodDef def = RpcRegistry.getMethodByRef(ref);
            CmsRpcMethodDefChoice choice;
            if (def != null) {
                choice = new CmsRpcMethodDefChoice().altMethod(
                        new CmsRpcMethodDef().version(def.version).timeout(def.timeout).request(def.requestDef).response(def.responseDef));
            } else {
                choice = new CmsRpcMethodDefChoice().altError(12); // TYPE_CONFLICT
            }
            resp.reference.add(choice);
        }
        return ok(resp, reqId);
    }
}
