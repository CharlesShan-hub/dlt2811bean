package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.data.CmsGetDataValuesError;
import com.ysh.jcms.pdu.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetDataValuesClient extends BaseClientHandler<GetDataValuesDao> {

    @Override
    public void execute(GetDataValuesDao dao) throws Exception {
        send(ServiceName.GET_DATA_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataValuesError err = decodeErr(frame, new CmsGetDataValuesError());
        throw new IOException("GetDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataValuesDao dao) throws IOException {
        CmsGetDataValuesResponse resp = decodeResp(frame, new CmsGetDataValuesResponse());
        if (content() != null) {
            content().res(resp.inner.toJsonValue());
        }
    }
}
