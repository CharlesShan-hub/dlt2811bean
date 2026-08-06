package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.control.CmsCancelError;
import com.ysh.jcms.pdu.control.CmsCancelResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class CancelClient extends BaseClientHandler<CancelDao> {

    @Override
    public void execute(CancelDao dao) throws Exception {
        send(ServiceName.CANCEL, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsCancelError());
        throw new IOException("Cancel rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsCancelResponse());
        log.info("Cancel succeeded");
    }
}
