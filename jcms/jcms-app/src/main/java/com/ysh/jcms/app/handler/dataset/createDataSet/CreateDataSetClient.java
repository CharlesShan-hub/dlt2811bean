package com.ysh.jcms.app.handler.dataset.createDataSet;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.dataset.CmsCreateDataSetError;
import com.ysh.jcms.core.pdu.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class CreateDataSetClient extends BaseClientHandler<CreateDataSetDao> {

    @Override
    public void execute(CreateDataSetDao dao) throws Exception {
        send(CmsServiceInfo.CREATE_DATA_SET, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsCreateDataSetError err = CmsFrameDecoder.decodeErr(frame, new CmsCreateDataSetError());
        throw new IOException("CreateDataSet rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsFrameDecoder.decodeResp(frame, new CmsCreateDataSetResponse());
    }
}
