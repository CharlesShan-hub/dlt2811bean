package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.control.CmsTimeActivatedOperateError;
import com.ysh.jcms.core.pdu.control.CmsTimeActivatedOperateResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class TimeActivatedOperateClient extends BaseClientHandler<TimeActivatedOperateDao> {

    @Override
    public void execute(TimeActivatedOperateDao dao) throws Exception {
        send(CmsServiceInfo.TIME_ACTIVATED_OPERATE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsTimeActivatedOperateError());
        throw new IOException("TimeActivatedOperate rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsTimeActivatedOperateResponse());
    }
}
