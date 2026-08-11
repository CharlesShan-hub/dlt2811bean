package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsRpcCallError;
import com.ysh.jcms.pdu.rpc.CmsRpcCallResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class RpcCallClient extends BaseClientHandler<RpcCallDao> {

    @Override
    public void execute(RpcCallDao dao) throws Exception {
        send(ServiceName.RPC_CALL, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsRpcCallError err = decodeErr(frame, new CmsRpcCallError());
        throw new IOException("RpcCall rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, RpcCallDao dao) throws IOException {
        CmsRpcCallResponse resp = decodeResp(frame, new CmsRpcCallResponse());
        content().res(resp);
    }
}
