package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryError;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryRequest;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetRpcMethodDirectoryClient extends BaseClientHandler {
    public void execute(String iface, String after) throws Exception {
        CmsGetRpcMethodDirectoryRequest req = new CmsGetRpcMethodDirectoryRequest();
        if (iface != null && !iface.isEmpty())
            req.interfaceName(iface);
        if (after != null && !after.isEmpty())
            req.referenceAfter(after);
        send(ServiceName.GET_RPC_METHOD_DIRECTORY, req);
    }
    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetRpcMethodDirectoryError());
        throw new IOException("GetRpcMethodDirectory rejected");
    }
    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetRpcMethodDirectoryResponse());
        log.info("GetRpcMethodDirectory succeeded");
    }
}
