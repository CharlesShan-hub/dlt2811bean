package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsSetUrcbEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetUrcbValuesClient extends BaseClientHandler {

    public SetUrcbValuesClient(CmsNode node) { super(node); }

    public void execute(SetUrcbValuesDao dao) throws Exception {
        CmsSetUrcbValuesRequest req = new CmsSetUrcbValuesRequest().reqId(nextReqId());
        req.urcb.add(new CmsSetUrcbEntry()
            .reference(dao.ref() != null ? dao.ref() : ""));
        send(ServiceName.SET_URCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetUrcbValuesError err = new CmsSetUrcbValuesError();
        err.decode(frame.asduBytes());
        throw new IOException("SetURCBValues rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetUrcbValuesResponse resp = new CmsSetUrcbValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SetURCBValues succeeded");
    }
}
