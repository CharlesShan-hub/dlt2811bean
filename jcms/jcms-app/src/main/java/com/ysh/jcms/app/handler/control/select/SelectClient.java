package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.control.CmsSelectError;
import com.ysh.jcms.pdu.control.CmsSelectRequest;
import com.ysh.jcms.pdu.control.CmsSelectResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class SelectClient extends BaseClientHandler {

    public void execute(String ref) throws Exception {
        CmsSelectRequest req = new CmsSelectRequest().reference(ref);
        send(ServiceName.SELECT, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsSelectError());
        throw new IOException("Select rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsSelectResponse());
        log.info("Select succeeded");
    }
}
