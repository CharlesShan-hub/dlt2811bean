package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.control.CmsSelectError;
import com.ysh.jcms.core.pdu.control.CmsSelectResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class SelectClient extends BaseClientHandler<SelectDao> {

    @Override
    public void execute(SelectDao dao) throws Exception {
        send(ServiceName.SELECT, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsSelectError());
        throw new IOException("Select rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsSelectResponse());
    }
}
