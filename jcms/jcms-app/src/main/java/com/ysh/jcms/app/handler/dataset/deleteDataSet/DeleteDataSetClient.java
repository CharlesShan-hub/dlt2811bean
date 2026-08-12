package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.core.pdu.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class DeleteDataSetClient extends BaseClientHandler<DeleteDataSetDao> {

    @Override
    public void execute(DeleteDataSetDao dao) throws Exception {
        send(CmsServiceInfo.DELETE_DATA_SET, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsDeleteDataSetError err = decodeErr(frame, new CmsDeleteDataSetError());
        throw new IOException("DeleteDataSet rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsDeleteDataSetResponse resp = new CmsDeleteDataSetResponse();
        resp.decode(frame.asduBytes());
    }
}
