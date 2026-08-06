package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.sg.CmsSelectActiveSgError;
import com.ysh.jcms.pdu.sg.CmsSelectActiveSgResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectActiveSgClient extends BaseClientHandler<SelectActiveSgDao> {

    @Override
    public void execute(SelectActiveSgDao dao) throws Exception {
        send(ServiceName.SELECT_ACTIVE_SG, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectActiveSgError err = decodeErr(frame, new CmsSelectActiveSgError());
        throw new IOException("SelectActiveSG rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSelectActiveSgResponse resp = decodeResp(frame, new CmsSelectActiveSgResponse());
        log.info("SelectActiveSG succeeded");
    }
}
