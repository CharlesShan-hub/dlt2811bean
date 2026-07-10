package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.log.CmsSetLcbValuesError;
import com.ysh.jcms.svc.log.CmsSetLcbValuesRequest;
import com.ysh.jcms.svc.log.CmsSetLcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetLcbValuesClient extends BaseClientHandler {

    public void execute(SetLcbValuesDao dao) throws Exception {
        CmsSetLcbValuesRequest req = dao.toRequest(nextReqId());
        send(ServiceName.SET_LCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetLcbValuesError err = decodeErr(frame, new CmsSetLcbValuesError());
        StringBuilder sb = new StringBuilder("SetLCBValues rejected:");
        for (int i = 0; i < err.result.count; i++) {
            if (err.result.items.get(i).errorPresent.value()) {
                sb.append(" entry[").append(i).append("] error=").append(err.result.items.get(i).error.value());
            }
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetLcbValuesResponse resp = decodeResp(frame, new CmsSetLcbValuesResponse());
        log.info("SetLCBValues succeeded");
    }
}
