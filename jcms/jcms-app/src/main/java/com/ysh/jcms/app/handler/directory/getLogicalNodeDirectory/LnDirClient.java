package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class LnDirClient extends BaseClientHandler {

    private int acsiClass;

    public void execute(LnDirDao dao) throws Exception {
        this.acsiClass = dao.acsiClass();
        CmsGetLogicalNodeDirectoryRequest req = new CmsGetLogicalNodeDirectoryRequest().reqId(nextReqId()).acsiClass(dao.acsiClass())
                .refAfter(dao.referenceAfter());

        if (dao.ldName() != null) {
            req.reference.choice(CmsReferenceChoice.LD_NAME);
            req.reference.altLdName.value(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.choice(CmsReferenceChoice.LN_REFERENCE);
            req.reference.altLnReference.value(dao.lnReference());
        }

        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException(
                "GetLogicalNodeDirectory rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());
        List<String> names = resp.refs();
        node.getContentManager().initNodeDir(acsiClass, names);
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass={}", names.size(), acsiClass);
    }
}
