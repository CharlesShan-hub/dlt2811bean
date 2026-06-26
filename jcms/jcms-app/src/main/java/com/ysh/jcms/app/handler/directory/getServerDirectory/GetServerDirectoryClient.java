package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetServerDirectoryClient extends BaseClientHandler {

    public GetServerDirectoryClient(CmsNode node) {
        super(node);
    }

    public void execute(GetServerDirectoryDao dao) throws Exception {
        CmsGetServerDirectoryRequest req = new CmsGetServerDirectoryRequest()
            .reqId(nextReqId())
            .objectClass(dao.objectClass());
        if (dao.referenceAfter() != null) {
            req.refAfter(dao.referenceAfter());
        }
        send(ServiceName.GET_SERVER_DIRECTORY, req.encode());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = new CmsGetServerDirectoryError();
        err.decode(frame.asduBytes());
        throw new IOException("GetServerDirectory rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse();
        resp.decode(frame.asduBytes());
        List<String> names = new ArrayList<>();
        for (int i = 0; i < resp.reference.count; i++) {
            names.add(new String(resp.reference.items.get(i).value()));
        }
        node.getContentManager().initServerDir(null, names);
        log.info("GetServerDirectory succeeded: {} logical devices", names.size());
    }
}
