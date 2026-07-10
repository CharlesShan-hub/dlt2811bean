package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.sg.CmsConfirmEditSgValuesError;
import com.ysh.jcms.svc.sg.CmsConfirmEditSgValuesRequest;
import com.ysh.jcms.svc.sg.CmsConfirmEditSgValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class ConfirmEditSgValuesClient extends BaseClientHandler {

    public ConfirmEditSgValuesClient(CmsNode node) {
        super(node);
    }

    public void execute(ConfirmEditSgValuesDao dao) throws Exception {
        CmsConfirmEditSgValuesRequest req = new CmsConfirmEditSgValuesRequest().reqId(nextReqId()).sgcbReference(dao.sgcbReference());

        send(ServiceName.CONFIRM_EDIT_SG_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsConfirmEditSgValuesError err = new CmsConfirmEditSgValuesError();
        err.decode(frame.asduBytes());
        throw new IOException("ConfirmEditSGValues rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsConfirmEditSgValuesResponse resp = new CmsConfirmEditSgValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("ConfirmEditSGValues succeeded");
    }
}
