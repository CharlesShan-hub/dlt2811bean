package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetRequest;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class DeleteDataSetClient extends BaseClientHandler {

    public void execute(DeleteDataSetDao dao) throws Exception {
        CmsDeleteDataSetRequest req = new CmsDeleteDataSetRequest().reqId(nextReqId()).datasetReference(dao.datasetReference());

        send(ServiceName.DELETE_DATA_SET, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsDeleteDataSetError err = decodeErr(frame, new CmsDeleteDataSetError());
        throw new IOException("DeleteDataSet rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsDeleteDataSetResponse resp = new CmsDeleteDataSetResponse();
        resp.decode(frame.asduBytes());
        log.info("DeleteDataSet succeeded");
    }
}
