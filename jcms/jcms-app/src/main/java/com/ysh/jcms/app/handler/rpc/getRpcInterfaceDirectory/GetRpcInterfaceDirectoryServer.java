package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryRequest;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryResponse;
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
        CmsGetRpcInterfaceDirectoryResponse resp = new CmsGetRpcInterfaceDirectoryResponse();
        for (String name : RpcRegistry.getInterfaceNames()) {
            resp.reference.add(new CmsString(name));
        }
        resp.moreFollows(false);
        log.info("GetRpcInterfaceDirectory: returning {} interface(s)", resp.reference.size());
        return ok(resp, reqId);
    }
}
