package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgError;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgRequest;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectActiveSgClient extends BaseClientHandler {

    public SelectActiveSgClient(CmsNode node) {
        super(node);
    }

    public void execute(SelectActiveSgDao dao) throws Exception {
        CmsSelectActiveSgRequest req = new CmsSelectActiveSgRequest()
            .reqId(nextReqId())
            .sgcbReference(dao.sgcbReference())
            .settingGroupNumber(dao.settingGroupNumber());

        send(ServiceName.SELECT_ACTIVE_SG, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectActiveSgError err = new CmsSelectActiveSgError();
        err.decode(frame.asduBytes());
        throw new IOException("SelectActiveSG rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSelectActiveSgResponse resp = new CmsSelectActiveSgResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SelectActiveSG succeeded");
    }
}
