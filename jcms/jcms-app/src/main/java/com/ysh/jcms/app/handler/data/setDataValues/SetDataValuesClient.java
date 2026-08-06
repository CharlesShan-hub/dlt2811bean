package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.data.CmsSetDataValuesError;
import com.ysh.jcms.pdu.data.CmsSetDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetDataValuesClient extends BaseClientHandler<SetDataValuesDao> {

    @Override
    public void execute(SetDataValuesDao dao) throws Exception {
        send(ServiceName.SET_DATA_VALUES, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetDataValuesError err = decodeErr(frame, new CmsSetDataValuesError());
        int errorCount = err.result.size();
        StringBuilder sb = new StringBuilder("SetDataValues rejected:");
        for (int i = 0; i < errorCount; i++) {
            sb.append(" [").append(i).append("] error=").append(err.result.get(i));
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetDataValuesResponse resp = decodeResp(frame, new CmsSetDataValuesResponse());
        log.info("SetDataValues succeeded");
    }
}
