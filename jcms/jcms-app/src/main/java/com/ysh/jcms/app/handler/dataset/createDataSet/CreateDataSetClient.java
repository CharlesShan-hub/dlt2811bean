package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetError;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.pdu.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class CreateDataSetClient extends BaseClientHandler {

    public void execute(CreateDataSetDao dao) throws Exception {
        CmsCreateDataSetRequest req = new CmsCreateDataSetRequest().reqId(nextReqId()).datasetReference(dao.datasetReference())
                .refAfter(dao.referenceAfter());

        for (CreateDataSetDao.MemberRef m : dao.members()) {
            req.memberData.add(new CmsDataRefFcEntry().reference(m.reference()).fc(m.fc()));
        }

        send(ServiceName.CREATE_DATA_SET, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsCreateDataSetError err = decodeErr(frame, new CmsCreateDataSetError());
        throw new IOException("CreateDataSet rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsCreateDataSetResponse());
        log.info("CreateDataSet succeeded");
    }
}
