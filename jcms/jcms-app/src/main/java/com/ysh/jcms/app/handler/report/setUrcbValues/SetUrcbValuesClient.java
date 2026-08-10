package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.report.CmsSetUrcbResult;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetUrcbValuesClient extends BaseClientHandler<SetUrcbValuesDao> {

    @Override
    public void execute(SetUrcbValuesDao dao) throws Exception {
        send(ServiceName.SET_URCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetUrcbValuesError err = decodeErr(frame, new CmsSetUrcbValuesError());
        StringBuilder sb = new StringBuilder("SetURCBValues rejected:");
        int i = 0;
        for (CmsSetUrcbResult r : err.result) {
            if (r.isPresent("error")) {
                sb.append(" entry[").append(i).append("] error=").append(r.error.value());
            }
            i++;
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetUrcbValuesResponse resp = decodeResp(frame, new CmsSetUrcbValuesResponse());
        log.info("SetURCBValues succeeded");
    }
}
