package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetError;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class CreateDataSetClient extends BaseClientHandler<CreateDataSetDao> {

    @Override
    public void execute(CreateDataSetDao dao) throws Exception {
        send(ServiceName.CREATE_DATA_SET, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsCreateDataSetError err = decodeErr(frame, new CmsCreateDataSetError());
        throw new IOException("CreateDataSet rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsCreateDataSetResponse());
        log.info("CreateDataSet succeeded");
    }
}
