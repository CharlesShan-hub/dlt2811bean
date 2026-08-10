package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.log.CmsSetLcbResult;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesError;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetLcbValuesClient extends BaseClientHandler<SetLcbValuesDao> {

    @Override
    public void execute(SetLcbValuesDao dao) throws Exception {
        send(ServiceName.SET_LCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetLcbValuesError err = decodeErr(frame, new CmsSetLcbValuesError());
        StringBuilder sb = new StringBuilder("SetLCBValues rejected:");
        for (int i = 0; i < err.result.size(); i++) {
            CmsSetLcbResult r = err.result.get(i);
            if (r.isPresent("error")) {
                sb.append(" entry[").append(i).append("] error=").append(r.error.value());
            }
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetLcbValuesResponse resp = decodeResp(frame, new CmsSetLcbValuesResponse());
    }
}
