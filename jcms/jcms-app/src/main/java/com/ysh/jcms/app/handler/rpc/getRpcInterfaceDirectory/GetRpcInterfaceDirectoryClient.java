package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDirectoryError;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDirectoryResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetRpcInterfaceDirectoryClient extends BaseClientHandler<GetRpcInterfaceDirectoryDao> {

    @Override
    public void execute(GetRpcInterfaceDirectoryDao dao) throws Exception {
        send(CmsServiceInfo.GET_RPC_INTERFACE_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetRpcInterfaceDirectoryError err = decodeErr(frame, new CmsGetRpcInterfaceDirectoryError());
        throw new IOException("GetRpcInterfaceDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetRpcInterfaceDirectoryDao dao) throws IOException {
        CmsGetRpcInterfaceDirectoryResponse resp = decodeResp(frame, new CmsGetRpcInterfaceDirectoryResponse());
        content().res(resp);
    }
}
