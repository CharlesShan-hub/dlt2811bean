package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetBrcbValuesClient extends BaseClientHandler {

    public SetBrcbValuesClient(CmsNode node) { super(node); }

    public void execute(SetBrcbValuesDao dao) throws Exception {
        send(ServiceName.SET_BRCB_VALUES, dao.toRequest(nextReqId()));
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetBrcbValuesError err = new CmsSetBrcbValuesError();
        err.decode(frame.asduBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < err.result.count; i++) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("entry[").append(i).append("]=err_" + err.result.items.get(i).error.value());
        }
        throw new IOException("SetBRCBValues rejected: " + sb);
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetBrcbValuesResponse resp = new CmsSetBrcbValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SetBRCBValues succeeded");
    }
}
