package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryRequest;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetRpcInterfaceDirectoryClient extends BaseClientHandler<GetRpcInterfaceDirectoryDao> {
    @Override
    public void execute(GetRpcInterfaceDirectoryDao dao) throws Exception {
        CmsGetRpcInterfaceDirectoryRequest req = new CmsGetRpcInterfaceDirectoryRequest();
        if (dao.after() != null && !dao.after().isEmpty())
            req.referenceAfter(dao.after());
        send(ServiceName.GET_RPC_INTERFACE_DIRECTORY, req);
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetRpcInterfaceDirectoryError());
        throw new IOException("GetRpcInterfaceDirectory rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetRpcInterfaceDirectoryResponse());
        log.info("GetRpcInterfaceDirectory succeeded");
    }
}
