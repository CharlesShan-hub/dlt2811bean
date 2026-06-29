package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LnDirClient extends BaseClientHandler {

    private int acsiClass;

    public LnDirClient(CmsNode node) {
        super(node);
    }

    public void execute(LnDirDao dao) throws Exception {
        this.acsiClass = dao.acsiClass();
        CmsGetLogicalNodeDirectoryRequest req = new CmsGetLogicalNodeDirectoryRequest()
            .reqId(nextReqId())
            .acsiClass(dao.acsiClass());

        if (dao.ldName() != null) {
            req.reference.choice(CmsReferenceChoice.LD_NAME);
            req.reference.altLdName.value(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.choice(CmsReferenceChoice.LN_REFERENCE);
            req.reference.altLnReference.value(dao.lnReference());
        }

        if (dao.referenceAfter() != null) {
            req.refAfter(dao.referenceAfter());
        }

        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = new CmsGetLogicalNodeDirectoryError();
        err.decode(frame.asduBytes());
        throw new IOException("GetLogicalNodeDirectory rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = new CmsGetLogicalNodeDirectoryResponse();
        resp.reference.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        List<String> names = new ArrayList<>();
        for (int i = 0; i < resp.reference.count; i++) {
            names.add(new String(resp.reference.items.get(i).value()));
        }
        node.getContentManager().initNodeDir(acsiClass, names);
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass={}", names.size(), acsiClass);
    }
}
