package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDefinitionError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetRpcInterfaceDefinitionClient extends BaseClientHandler<GetRpcInterfaceDefinitionDao> {
    @Override
    public void execute(GetRpcInterfaceDefinitionDao dao) throws Exception {
        send(ServiceName.GET_RPC_INTERFACE_DEFINITION, dao);
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetRpcInterfaceDefinitionError());
        throw new IOException("GetRpcInterfaceDefinition rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetRpcInterfaceDefinitionResponse());
    }
}
