package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryRequest;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetRpcInterfaceDirectoryServer
        extends
            BaseServerHandler<CmsGetRpcInterfaceDirectoryRequest, CmsGetRpcInterfaceDirectoryError> {
    public GetRpcInterfaceDirectoryServer() {
        super(ServiceName.GET_RPC_INTERFACE_DIRECTORY, CmsGetRpcInterfaceDirectoryRequest.class, CmsGetRpcInterfaceDirectoryError.class);
    }
    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetRpcInterfaceDirectoryRequest req, int reqId) {
        log.info("GetRpcInterfaceDirectory from {}", session.getSessionId());
        CmsGetRpcInterfaceDirectoryResponse resp = new CmsGetRpcInterfaceDirectoryResponse();
        for (String name : RpcRegistry.getInterfaceNames()) {
            resp.reference.add(new CmsString(name));
        }
        resp.moreFollows(false);
        log.info("GetRpcInterfaceDirectory: returning {} interface(s)", resp.reference.size());
        return ok(resp, reqId);
    }
}
