package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateError;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class TimeActivatedOperateClient extends BaseClientHandler<TimeActivatedOperateDao> {

    @Override
    public void execute(TimeActivatedOperateDao dao) throws Exception {
        send(ServiceName.TIME_ACTIVATED_OPERATE, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsTimeActivatedOperateError());
        throw new IOException("TimeActivatedOperate rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsTimeActivatedOperateResponse());
        log.info("TimeActivatedOperate scheduled");
    }
}
