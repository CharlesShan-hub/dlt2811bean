package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.control.CmsOperateError;
import com.ysh.jcms.core.pdu.control.CmsOperateResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class OperateClient extends BaseClientHandler<OperateDao> {

    @Override
    public void execute(OperateDao dao) throws Exception {
        send(CmsServiceInfo.OPERATE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsOperateError());
        throw new IOException("Operate rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsOperateResponse());
    }
}
