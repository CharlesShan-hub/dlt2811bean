package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetDataSetValuesClient extends BaseClientHandler<SetDataSetValuesDao> {

    @Override
    public void execute(SetDataSetValuesDao dao) throws Exception {
        send(ServiceName.SET_DATA_SET_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetDataSetValuesError err = decodeErr(frame, new CmsSetDataSetValuesError());
        StringBuilder sb = new StringBuilder("SetDataSetValues partially failed: ");
        for (int i = 0; i < err.result.size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append("[").append(i).append("]=").append(err.result.get(i));
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetDataSetValuesResponse resp = new CmsSetDataSetValuesResponse();
        resp.decode(frame.asduBytes());
        log.info("SetDataSetValues succeeded");
    }
}
