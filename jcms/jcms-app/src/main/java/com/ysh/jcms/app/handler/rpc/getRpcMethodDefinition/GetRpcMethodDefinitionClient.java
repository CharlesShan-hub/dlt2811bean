package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.rpc.CmsGetRpcMethodDefinitionError;
import com.ysh.jcms.svc.rpc.CmsGetRpcMethodDefinitionRequest;
import com.ysh.jcms.svc.rpc.CmsGetRpcMethodDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.util.List;

public class GetRpcMethodDefinitionClient extends BaseClientHandler {
    public void execute(List<String> refs) throws Exception {
        CmsGetRpcMethodDefinitionRequest req = new CmsGetRpcMethodDefinitionRequest().reqId(nextReqId());
        for (String ref : refs) {
            CmsUint8Array r = new CmsUint8Array();
            r.value(ref);
            req.reference.add(r);
        }
        send(ServiceName.GET_RPC_METHOD_DEFINITION, req);
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
