package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionError;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetRpcInterfaceDefinitionClient extends BaseClientHandler<GetRpcInterfaceDefinitionDao> {

    @Override
    public void execute(GetRpcInterfaceDefinitionDao dao) throws Exception {
        send(CmsServiceInfo.GET_RPC_INTERFACE_DEFINITION, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetRpcInterfaceDefinitionError err = decodeErr(frame, new CmsGetRpcInterfaceDefinitionError());
        throw new IOException("GetRpcInterfaceDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetRpcInterfaceDefinitionDao dao) throws IOException {
        CmsGetRpcInterfaceDefinitionResponse resp = decodeResp(frame, new CmsGetRpcInterfaceDefinitionResponse());
        content().res(resp);
    }
}
