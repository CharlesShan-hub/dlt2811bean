package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetUrcbValuesClient extends BaseClientHandler {

    public void execute(SetUrcbValuesDao dao) throws Exception {
        CmsSetUrcbValuesRequest req = dao.toRequest(nextReqId());
        send(ServiceName.SET_URCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetUrcbValuesError err = decodeErr(frame, new CmsSetUrcbValuesError());
        StringBuilder sb = new StringBuilder("SetURCBValues rejected:");
        for (int i = 0; i < err.result.count; i++) {
            if (err.result.items.get(i).errorPresent.value()) {
                sb.append(" entry[").append(i).append("] error=").append(err.result.items.get(i).error.value());
            }
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetUrcbValuesResponse resp = decodeResp(frame, new CmsSetUrcbValuesResponse());
        log.info("SetURCBValues succeeded");
    }
}
