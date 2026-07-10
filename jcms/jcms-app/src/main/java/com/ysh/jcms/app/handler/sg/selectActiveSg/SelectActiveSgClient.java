package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgError;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgRequest;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectActiveSgClient extends BaseClientHandler {

    public void execute(SelectActiveSgDao dao) throws Exception {
        CmsSelectActiveSgRequest req = new CmsSelectActiveSgRequest().reqId(nextReqId()).sgcbReference(dao.sgcbReference())
                .settingGroupNumber(dao.settingGroupNumber());

        send(ServiceName.SELECT_ACTIVE_SG, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectActiveSgError err = decodeErr(frame, new CmsSelectActiveSgError());
        throw new IOException("SelectActiveSG rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSelectActiveSgResponse resp = decodeResp(frame, new CmsSelectActiveSgResponse());
        log.info("SelectActiveSG succeeded");
    }
}
