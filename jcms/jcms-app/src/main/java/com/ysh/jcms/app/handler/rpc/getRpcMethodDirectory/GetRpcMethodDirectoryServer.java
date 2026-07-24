package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.rpc.CmsGetRpcMethodDirectoryError;
import com.ysh.jcms.svc.rpc.CmsGetRpcMethodDirectoryRequest;
import com.ysh.jcms.svc.rpc.CmsGetRpcMethodDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRpcMethodDirectoryServer extends BaseServerHandler {
    private static final Logger log = LoggerFactory.getLogger(GetRpcMethodDirectoryServer.class);
    public GetRpcMethodDirectoryServer() {
        super(ServiceName.GET_RPC_METHOD_DIRECTORY, CmsGetRpcMethodDirectoryRequest.class, CmsGetRpcMethodDirectoryError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetRpcMethodDirectoryRequest req = (CmsGetRpcMethodDirectoryRequest) rawReq;
        String iface = str(req.interfaceName);
        log.info("GetRpcMethodDirectory from {}: interface={}", session.getSessionId(), iface);

        CmsGetRpcMethodDirectoryResponse resp = new CmsGetRpcMethodDirectoryResponse().reqId(reqId);

        if (iface == null) {
            // Return all methods from all interfaces, using full ref format
            for (String ifName : RpcRegistry.getInterfaceNames()) {
                for (String methodName : RpcRegistry.getMethodNames(ifName)) {
                    CmsUint8Array ref = new CmsUint8Array();
                    ref.value(ifName + "." + methodName);
                    resp.reference.add(ref);
                }
            }
        } else {
            RpcRegistry.InterfaceDef def = RpcRegistry.getInterface(iface);
            if (def == null)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            for (String methodName : RpcRegistry.getMethodNames(iface)) {
                CmsUint8Array ref = new CmsUint8Array();
                ref.value(methodName);
                resp.reference.add(ref);
            }
        }
        resp.moreFollows(false);
        log.info("GetRpcMethodDirectory: returning {} method(s)", resp.reference.count);
        return ok(resp, reqId);
    }
}
