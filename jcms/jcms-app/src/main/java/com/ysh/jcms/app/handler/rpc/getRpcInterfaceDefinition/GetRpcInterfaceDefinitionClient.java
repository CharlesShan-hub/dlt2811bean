package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDefinitionError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDefinitionRequest;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetRpcInterfaceDefinitionClient extends BaseClientHandler {
    public void execute(String iface, String after) throws Exception {
        CmsGetRpcInterfaceDefinitionRequest req = new CmsGetRpcInterfaceDefinitionRequest().interfaceName(iface);
        if (after != null && !after.isEmpty())
            req.referenceAfter(after);
        send(ServiceName.GET_RPC_INTERFACE_DEFINITION, req);
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetRpcInterfaceDefinitionError());
        throw new IOException("GetRpcInterfaceDefinition rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetRpcInterfaceDefinitionResponse());
        log.info("GetRpcInterfaceDefinition succeeded");
    }
}
