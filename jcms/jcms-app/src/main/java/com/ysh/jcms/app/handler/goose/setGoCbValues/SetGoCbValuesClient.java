package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.goose.CmsSetGoCbResult;
import com.ysh.jcms.pdu.goose.CmsSetGoCbValuesError;
import com.ysh.jcms.pdu.goose.CmsSetGoCbValuesRequest;
import com.ysh.jcms.pdu.goose.CmsSetGoCbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetGoCbValuesClient extends BaseClientHandler {

    public void execute(SetGoCbValuesDao dao) throws Exception {
        CmsSetGoCbValuesRequest req = dao.toRequest(nextReqId());
        send(ServiceName.SET_GOCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetGoCbValuesError err = decodeErr(frame, new CmsSetGoCbValuesError());
        StringBuilder sb = new StringBuilder("SetGoCBValues rejected:");
        int i = 0;
        for (CmsSetGoCbResult r : err.result) {
            if (r.isPresent("error")) {
                sb.append(" entry[").append(i).append("] error=").append(r.error.value());
            }
            i++;
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetGoCbValuesResponse resp = decodeResp(frame, new CmsSetGoCbValuesResponse());
        log.info("SetGoCBValues succeeded");
    }
}
