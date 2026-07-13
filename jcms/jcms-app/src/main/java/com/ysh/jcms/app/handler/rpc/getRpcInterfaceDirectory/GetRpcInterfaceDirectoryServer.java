package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.rpc.CmsGetRpcInterfaceDirectoryError;
import com.ysh.jcms.svc.rpc.CmsGetRpcInterfaceDirectoryRequest;
import com.ysh.jcms.svc.rpc.CmsGetRpcInterfaceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRpcInterfaceDirectoryServer extends BaseServerHandler {
    private static final Logger log = LoggerFactory.getLogger(GetRpcInterfaceDirectoryServer.class);
    public GetRpcInterfaceDirectoryServer() {
        super(ServiceName.GET_RPC_INTERFACE_DIRECTORY, CmsGetRpcInterfaceDirectoryRequest.class, CmsGetRpcInterfaceDirectoryError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        log.info("GetRpcInterfaceDirectory from {}", session.getSessionId());
        CmsGetRpcInterfaceDirectoryResponse resp = new CmsGetRpcInterfaceDirectoryResponse().reqId(reqId);
        for (String name : RpcRegistry.getInterfaceNames()) {
            CmsUint8Array ref = new CmsUint8Array();
            ref.value(name);
            resp.reference.add(ref);
        }
        resp.moreFollows(false);
        log.info("GetRpcInterfaceDirectory: returning {} interface(s)", resp.reference.count);
        return ok(resp, reqId);
    }
}
