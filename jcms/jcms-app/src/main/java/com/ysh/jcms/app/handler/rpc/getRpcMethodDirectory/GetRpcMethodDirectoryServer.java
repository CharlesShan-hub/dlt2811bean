package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryRequest;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetRpcMethodDirectoryServer extends BaseServerHandler<CmsGetRpcMethodDirectoryRequest, CmsGetRpcMethodDirectoryError> {
    public GetRpcMethodDirectoryServer() {
        super(ServiceName.GET_RPC_METHOD_DIRECTORY, CmsGetRpcMethodDirectoryRequest.class, CmsGetRpcMethodDirectoryError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetRpcMethodDirectoryRequest req, int reqId) {
        String iface = str(req.interfaceName);
        log.info("GetRpcMethodDirectory from {}: interface={}", session.getSessionId(), iface);

        CmsGetRpcMethodDirectoryResponse resp = new CmsGetRpcMethodDirectoryResponse();

        if (iface == null) {
            // Return all methods from all interfaces, using full ref format
            for (String ifName : RpcRegistry.getInterfaceNames()) {
                for (String methodName : RpcRegistry.getMethodNames(ifName)) {
                    resp.reference.add(new CmsString(ifName + "." + methodName));
                }
            }
        } else {
            RpcRegistry.InterfaceDef def = RpcRegistry.getInterface(iface);
            if (def == null)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            for (String methodName : RpcRegistry.getMethodNames(iface)) {
                resp.reference.add(new CmsString(methodName));
            }
        }
        resp.moreFollows(false);
        log.info("GetRpcMethodDirectory: returning {} method(s)", resp.reference.size());
        return ok(resp, reqId);
    }
}
