package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDefinitionError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetRpcMethodDefinitionClient extends BaseClientHandler<GetRpcMethodDefinitionDao> {
    @Override
    public void execute(GetRpcMethodDefinitionDao dao) throws Exception {
        send(ServiceName.GET_RPC_METHOD_DEFINITION, dao);
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetRpcMethodDefinitionError());
        throw new IOException("GetRpcMethodDefinition rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetRpcMethodDefinitionResponse());
        log.info("GetRpcMethodDefinition succeeded");
    }
}
