package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.report.CmsSetUrcbResult;
import com.ysh.jcms.core.pdu.report.CmsSetUrcbValuesError;
import com.ysh.jcms.core.pdu.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.Collections;

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
    protected void onSuccess(Frame frame, SetUrcbValuesDao dao) throws IOException {
        decodeResp(frame, new CmsSetUrcbValuesResponse());
        content().res(Collections.singletonList("URCB values set successfully"));
    }
}
