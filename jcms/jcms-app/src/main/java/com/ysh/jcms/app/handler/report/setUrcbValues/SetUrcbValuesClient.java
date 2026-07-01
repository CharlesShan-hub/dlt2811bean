package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetUrcbValuesClient extends BaseClientHandler {

    public SetUrcbValuesClient(CmsNode node) { super(node); }

    public void execute(SetUrcbValuesDao dao) throws Exception {
        CmsSetUrcbValuesRequest req = dao.toRequest(nextReqId());
        send(ServiceName.SET_URCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetUrcbValuesError err = new CmsSetUrcbValuesError();
        err.result.allocSize = 64;
        err.decode(frame.asduBytes());
        StringBuilder sb = new StringBuilder("SetURCBValues rejected:");
        for (int i = 0; i < err.result.count; i++) {
            if (err.result.items.get(i).errorPresent.value()) {
                sb.append(" entry[").append(i).append("] error=")
                  .append(err.result.items.get(i).error.value());
            }
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetUrcbValuesResponse resp = new CmsSetUrcbValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SetURCBValues succeeded");
    }
}
