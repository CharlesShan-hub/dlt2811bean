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
        send(ServiceName.RPC_CALL, dao.toRequest());
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsRpcCallError());
        throw new IOException("RpcCall rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsRpcCallResponse());
        log.info("RpcCall succeeded");
    }
}
