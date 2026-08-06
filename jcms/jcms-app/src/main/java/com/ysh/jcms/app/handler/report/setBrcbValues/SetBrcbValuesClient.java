package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.report.CmsSetBrcbResult;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesError;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetBrcbValuesClient extends BaseClientHandler<SetBrcbValuesDao> {

    @Override
    public void execute(SetBrcbValuesDao dao) throws Exception {
        CmsSetBrcbValuesRequest req = dao.toRequest(nextReqId());
        send(ServiceName.SET_BRCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetBrcbValuesError err = decodeErr(frame, new CmsSetBrcbValuesError());
        StringBuilder sb = new StringBuilder("SetBRCBValues rejected:");
        int i = 0;
        for (CmsSetBrcbResult r : err.result) {
            if (r.isPresent("error")) {
                sb.append(" entry[").append(i).append("] error=").append(r.error.value());
            }
            i++;
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetBrcbValuesResponse resp = decodeResp(frame, new CmsSetBrcbValuesResponse());
        log.info("SetBRCBValues succeeded");
    }
}
