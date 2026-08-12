package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbResult;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbValuesError;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.Collections;

public class SetBrcbValuesClient extends BaseClientHandler<SetBrcbValuesDao> {

    @Override
    public void execute(SetBrcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.SET_BRCB_VALUES, dao);
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
    protected void onSuccess(Frame frame, SetBrcbValuesDao dao) throws IOException {
        decodeResp(frame, new CmsSetBrcbValuesResponse());
        content().res(Collections.singletonList("BRCB values set successfully"));
    }
}
