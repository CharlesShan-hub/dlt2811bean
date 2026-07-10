package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.goose.CmsSetGoCbValuesError;
import com.ysh.jcms.svc.goose.CmsSetGoCbValuesRequest;
import com.ysh.jcms.svc.goose.CmsSetGoCbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetGoCbValuesClient extends BaseClientHandler {

    public SetGoCbValuesClient(CmsNode node) {
        super(node);
    }

    public void execute(SetGoCbValuesDao dao) throws Exception {
        CmsSetGoCbValuesRequest req = dao.toRequest(nextReqId());
        send(ServiceName.SET_GOCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetGoCbValuesError err = new CmsSetGoCbValuesError();
        err.decode(frame.asduBytes());
        StringBuilder sb = new StringBuilder("SetGoCBValues rejected:");
        for (int i = 0; i < err.result.count; i++) {
            if (err.result.items.get(i).errorPresent.value()) {
                sb.append(" entry[").append(i).append("] error=").append(err.result.items.get(i).error.value());
            }
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetGoCbValuesResponse resp = new CmsSetGoCbValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SetGoCBValues succeeded");
    }
}
