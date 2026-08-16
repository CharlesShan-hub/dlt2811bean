package com.ysh.jcms.app.handler.control.cancel;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.control.CmsCancelError;
import com.ysh.jcms.core.pdu.control.CmsCancelResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class CancelClient extends BaseClientHandler<CancelDao> {

    @Override
    public void execute(CancelDao dao) throws Exception {
        send(CmsServiceInfo.CANCEL, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsFrameDecoder.decodeErr(frame, new CmsCancelError());
        throw new IOException("Cancel rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsFrameDecoder.decodeResp(frame, new CmsCancelResponse());
    }
}
