package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class SvrDirClient extends BaseClientHandler {

    public void execute(SvrDirDao dao) throws Exception {
        CmsGetServerDirectoryRequest req = new CmsGetServerDirectoryRequest().reqId(nextReqId()).objectClass(dao.objectClass())
                .refAfter(dao.referenceAfter());
        send(ServiceName.GET_SERVER_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());
        List<String> names = resp.ldNames();
        node.getContentManager().initServerDir(names);
        log.info("GetServerDirectory succeeded: {} logical devices", names.size());
    }
}
