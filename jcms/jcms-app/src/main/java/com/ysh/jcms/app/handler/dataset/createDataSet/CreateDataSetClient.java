package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetError;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class CreateDataSetClient extends BaseClientHandler {

    public CreateDataSetClient(CmsNode node) {
        super(node);
    }

    public void execute(CreateDataSetDao dao) throws Exception {
        CmsCreateDataSetRequest req = new CmsCreateDataSetRequest().reqId(nextReqId()).datasetReference(dao.datasetReference());

        if (dao.referenceAfter() != null && !dao.referenceAfter().isEmpty()) {
            req.refAfter(dao.referenceAfter());
        }

        for (CreateDataSetDao.MemberRef m : dao.members()) {
            req.memberData.add(new CmsDataRefFcEntry().reference(m.reference()).fc(m.fc()));
        }

        send(ServiceName.CREATE_DATA_SET, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsCreateDataSetError err = new CmsCreateDataSetError();
        err.decode(frame.asduBytes());
        throw new IOException("CreateDataSet rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsCreateDataSetResponse resp = new CmsCreateDataSetResponse();
        resp.decode(frame.asduBytes());
        log.info("CreateDataSet succeeded");
    }
}
