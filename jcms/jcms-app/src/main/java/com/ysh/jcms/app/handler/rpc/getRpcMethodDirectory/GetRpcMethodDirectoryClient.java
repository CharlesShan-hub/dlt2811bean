package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDirectoryError;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDirectoryResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetRpcMethodDirectoryClient extends BaseClientHandler<GetRpcMethodDirectoryDao> {

    @Override
    public void execute(GetRpcMethodDirectoryDao dao) throws Exception {
        send(CmsServiceInfo.GET_RPC_METHOD_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetRpcMethodDirectoryError err = decodeErr(frame, new CmsGetRpcMethodDirectoryError());
        throw new IOException("GetRpcMethodDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetRpcMethodDirectoryDao dao) throws IOException {
        CmsGetRpcMethodDirectoryResponse resp = decodeResp(frame, new CmsGetRpcMethodDirectoryResponse());
        content().res(resp);
    }
}
