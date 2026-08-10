package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetRpcMethodDirectoryClient extends BaseClientHandler<GetRpcMethodDirectoryDao> {
    @Override
    public void execute(GetRpcMethodDirectoryDao dao) throws Exception {
        send(ServiceName.GET_RPC_METHOD_DIRECTORY, dao);
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetRpcMethodDirectoryError());
        throw new IOException("GetRpcMethodDirectory rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetRpcMethodDirectoryResponse());
    }
}
