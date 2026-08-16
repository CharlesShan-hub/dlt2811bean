package com.ysh.jcms.app.handler.data.getDataDefinition;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.data.CmsGetDataDefinitionError;
import com.ysh.jcms.core.pdu.data.CmsGetDataDefinitionResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetDataDefinitionClient extends BaseClientHandler<GetDataDefinitionDao> {

    @Override
    public void execute(GetDataDefinitionDao dao) throws Exception {
        send(CmsServiceInfo.GET_DATA_DEFINITION, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDefinitionError err = CmsFrameDecoder.decodeErr(frame, new CmsGetDataDefinitionError());
        throw new IOException("GetDataDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataDefinitionDao dao) throws IOException {
        CmsGetDataDefinitionResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetDataDefinitionResponse());
        if (content() != null) {
            content().res(resp.inner.toJsonValue());
        }
    }
}
