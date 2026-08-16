package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDefinitionError;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDefinitionResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetRpcMethodDefinitionClient extends BaseClientHandler<GetRpcMethodDefinitionDao> {

    @Override
    public void execute(GetRpcMethodDefinitionDao dao) throws Exception {
        send(CmsServiceInfo.GET_RPC_METHOD_DEFINITION, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetRpcMethodDefinitionError err = CmsFrameDecoder.decodeErr(frame, new CmsGetRpcMethodDefinitionError());
        throw new IOException("GetRpcMethodDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetRpcMethodDefinitionDao dao) throws IOException {
        CmsGetRpcMethodDefinitionResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetRpcMethodDefinitionResponse());
        content().res(resp);
    }
}
