package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLogicalNodeDirectoryClient extends BaseClientHandler {

    public GetLogicalNodeDirectoryClient(CmsNode node) {
        super(node);
    }

    public void execute(GetLogicalNodeDirectoryDao dao) throws Exception {
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

        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, req.encode());
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
        resp.decode(frame.asduBytes());
        List<String> names = new ArrayList<>();
        for (int i = 0; i < resp.reference.count; i++) {
            names.add(new String(resp.reference.items.get(i).value()));
        }
        // Note: acsiClass is not in the response, so we rely on the caller
        // to have set it in the DAO. The ContentManager stores by acsiClass.
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass unknown here", names.size());
    }
}
